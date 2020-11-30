package capstone.oras.amazonS3.controller;

import capstone.oras.amazonS3.client.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/s3-management/")
@CrossOrigin(value = "http://localhost:4200")
public class AmazonS3Controller {
    private AmazonClient amazonClient;
    private class AmazonResponse{
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    @Autowired
    AmazonS3Controller(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/uploadFile")
    public AmazonResponse uploadFile(@RequestPart(value = "file") MultipartFile file) {
        AmazonResponse amazonResponse = new AmazonResponse();
        amazonResponse.setUrl(this.amazonClient.uploadFile(file));
        return amazonResponse;
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }
}
