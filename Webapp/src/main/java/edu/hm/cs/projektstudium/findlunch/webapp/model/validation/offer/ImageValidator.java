package edu.hm.cs.projektstudium.findlunch.webapp.model.validation.offer;

import edu.hm.cs.projektstudium.findlunch.webapp.model.Offer;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ImageValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Offer.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Offer offer = (Offer) target;

        List<MultipartFile> multipartFiles = new ArrayList<MultipartFile>();
        if(offer.getFirstOfferImage() != null) {
            multipartFiles.add(offer.getFirstOfferImage());
        }

        if(offer.getSecondOfferImage() != null) {
            multipartFiles.add(offer.getSecondOfferImage());
        }

        if(offer.getThirdOfferImage() != null) {
            multipartFiles.add(offer.getThirdOfferImage());
        }

        for(MultipartFile multipartFile : multipartFiles) {
            if(multipartFile.getSize() > 0) {
                double fileSizeInMegabytes = multipartFile.getSize() / (1024 * 1024);
                String fileType = multipartFile.getContentType();

                if(fileSizeInMegabytes > 5) {
                    errors.reject("offer.validation.imageSize");
                }

                if(!(fileType.equals("image/jpeg"))) {
                    errors.reject("offer.validation.imageType");
                }
            }
        }
    }
}
