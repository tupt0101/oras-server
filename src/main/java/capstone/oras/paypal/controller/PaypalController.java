package capstone.oras.paypal.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(path = "/v1/paypal")
@CrossOrigin(value = "http://localhost:9527")
public class PaypalController {

    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaypalService paypalService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/pay/{price}", method = RequestMethod.GET)
    public String pay(HttpServletRequest request,@PathVariable("price") double price ){
        String cancelUrl = Utils.getBaseURL(request) + "/v1/paypal/" + URL_PAYPAL_CANCEL;
        String successUrl = Utils.getBaseURL(request) + "/v1/paypal/" + URL_PAYPAL_SUCCESS;
        try {
            Payment payment = paypalService.createPayment(
                    price,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl);
            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
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
    public String cancelPay(){
        return "cancel";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pay/success")
    @ResponseBody
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                return "success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

}
