//package com.ren.productpicture;
//
//import com.ren.productpicture.entity.ProductPicture;
//import com.ren.productpicture.service.impl.ProductPictureServiceImpl;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class FileUploadConsumer {
//
//    @Autowired
//    private ProductPictureServiceImpl productPictureSvc;
//
//    @RabbitListener(queues = "#{queue.name}")
//    public void receiveMessage(ProductPicture productPicture) throws IOException {
//        String sessionId = productPicture.getSessionId();
//        String fileType = productPicture.getMimeType();
//        byte[] uploadFile = productPicture.getProductPic();
//
//        // 發送進度更新
//        productPictureSvc.sendProgress(sessionId, 0);
//
//        // 檢查檔案類別，如果是jpeg or png等已壓縮檔案，直接上傳，如果不是，執行壓縮
//        if (validateFileType(fileType)) {
//            System.out.println("不需要壓縮!");
//            productPictureSvc.addProductPicture(productPicture);
//            productPictureSvc.sendProgress(sessionId, 100);
//        } else {
//            System.out.println("看來需要壓縮哦!");
//            // 模擬壓縮進度
//            for (int i = 1; i <= 10; i++) {
//                try {
//                    Thread.sleep(500); // 模擬壓縮耗時
//                    productPictureSvc.sendProgress(sessionId, i * 10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            productPictureSvc.storeFile(uploadFile, productPicture);
//        }
//
//        productPictureSvc.sendProgress(sessionId, 100); // 完成進度
//    }
//
//    private boolean validateFileType(String fileType) {
//        // 檢查文件類型的邏輯
//        return "image/jpeg".equals(fileType) || "image/png".equals(fileType);
//    }
//}
