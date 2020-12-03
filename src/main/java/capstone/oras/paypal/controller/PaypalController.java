package capstone.oras.paypal.controller;

import capstone.oras.api.accountPackage.service.IAccountPackageService;
import capstone.oras.api.packages.service.IPackageService;
import capstone.oras.api.purchase.service.IPurchaseService;
import capstone.oras.entity.AccountPackageEntity;
import capstone.oras.entity.PurchaseEntity;
import capstone.oras.paypal.config.PaypalPaymentIntent;
import capstone.oras.paypal.config.PaypalPaymentMethod;
import capstone.oras.paypal.service.PaypalService;
import capstone.oras.paypal.utils.Utils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@Controller
@RequestMapping(path = "/v1/paypal")
@CrossOrigin(value = "http://localhost:9527")
public class PaypalController {

    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPurchaseService purchaseService;

    @Autowired
    private IAccountPackageService accountPackageService;

    @Autowired
    private IPackageService packageService;

    @Autowired
    private PaypalService paypalService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/pay/{price}", method = RequestMethod.GET)
    public String pay(HttpServletRequest request, @PathVariable("price") double price, @RequestParam(value = "accountId", required = true) int accountId, @RequestParam(value = "packageId", required = true) int packageId) {
        AccountPackageEntity accountPackageEntity = accountPackageService.findAccountPackageByAccountId(accountId);
        if (accountPackageEntity != null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Account still has a usable package");
        }
        String cancelUrl = Utils.getBaseURL(request) + "/v1/paypal/" + URL_PAYPAL_CANCEL;
        String successUrl = Utils.getBaseURL(request) + "/v1/paypal/" + URL_PAYPAL_SUCCESS + "/" + accountId + "/" + packageId;
        try {
            Payment payment = paypalService.createPayment(
                    price,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping(URL_PAYPAL_CANCEL)
    @ResponseBody
    public String cancelPay() {
        return "cancel";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pay/success/{accountId}/{packageId}")
    @ResponseBody
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, @RequestParam("token") String token, @PathVariable("accountId") int accountId, @PathVariable("packageId") int packageId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                PurchaseEntity purchaseEntity = new PurchaseEntity();
                purchaseEntity.setAccountId(accountId);
                purchaseEntity.setAmount(Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal()));
                purchaseEntity.setPayerId(payerId);
                purchaseEntity.setPurchaseDate(LocalDateTime.now());
                purchaseEntity.setStatus("success");
                purchaseEntity.setToken(token);
                purchaseEntity.setPaymentId(paymentId);
                purchaseEntity = purchaseService.createPurchase(purchaseEntity);

                AccountPackageEntity accountPackageEntity = new AccountPackageEntity();
                accountPackageEntity.setAccountId(accountId);
                accountPackageEntity.setPackageId(packageId);
                accountPackageEntity.setPurchaseId(purchaseEntity.getId());
                accountPackageEntity.setValidTo(LocalDateTime.now().plusMonths(1));
                accountPackageEntity.setNumOfPost(packageService.findPackageById(packageId).getNumOfPost());
                accountPackageService.createAccountPackage(accountPackageEntity);


                return "<HTML><body> <a href=\"http://localhost:9527/#\">Payment Successful (Click to go back)</a></body></HTML>";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

}
