package com;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.howard.rentalorder.dto.CheckoutPaymentRequestForm;
import com.howard.rentalorder.dto.ProductForm;
import com.howard.rentalorder.dto.ProductPackageForm;
import com.howard.rentalorder.dto.RedirectUrls;
import ecpay.logistics.integration.AllInOne;
import ecpay.logistics.integration.domain.QueryLogisticsTradeInfoObj;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static com.utils.JsonUtil.toJson;

@Configuration
@EnableAspectJAutoProxy
@EnableAsync
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories("com.*")
@EntityScan(basePackages = {"com.*"})
@ServletComponentScan
@EnableScheduling
@SpringBootApplication
public class Cia101G2Application {

	public static void main(String[] args) {
		SpringApplication.run(Cia101G2Application.class, args);

		// 測試查詢物流動態

//		AllInOne all = new AllInOne();
//		String statUpdated = postQueryLogisticsTradeInfo(all);
//		System.out.println(statUpdated);



		//測試rentalFAV 進 redis

//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//			// RequestApi
//			// 此處對應下方Request Body 所需的json格式物件
//			CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();
//			form.setAmount(new BigDecimal("100"));
//			form.setCurrency("TWD");
//			form.setOrderId("123451237");
//
//			ProductPackageForm productPackageForm = new ProductPackageForm();
//			productPackageForm.setId("1");
//			productPackageForm.setName("fallElove");
//			productPackageForm.setAmount(new BigDecimal("100"));
//
//			ProductForm productForm = new ProductForm();
//			productForm.setId("1");
//			productForm.setName("testPackage");
//			productForm.setImageUrl("");
//			productForm.setQuantity(new BigDecimal("10"));
//			productForm.setPrice(new BigDecimal("10"));
//			productPackageForm.setProducts(Arrays.asList(productForm));
//
//			form.setPackages(Arrays.asList(productPackageForm));
//			RedirectUrls redirectUrls = new RedirectUrls();
//			redirectUrls.setConfirmUrl("https://www.google.com");
//			form.setRedirectUrls(redirectUrls);
//
//			String nonce = UUID.randomUUID().toString();
//			String requestBody = objectMapper.writeValueAsString(form);
//			System.out.println("body =>" + requestBody);
//			System.out.println("nonce =>" + nonce);
//			String signature = encrypt(CHANNEL_SECRET, CHANNEL_SECRET + REQUEST_URL + objectMapper.writeValueAsString(form) + nonce);
//			System.out.println("有設定好簽名了====" + signature);
//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON);
//			headers.set("X-LINE-ChannelId", CHANNEL_ID);
//			headers.set("X-LINE-Authorization-Nonce", nonce);
//			headers.set("X-LINE-Authorization", signature);
//			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
//			RestTemplate restTemplate = new RestTemplate();
//			ResponseEntity<String> response = restTemplate.exchange(REQUEST_URL, HttpMethod.POST, entity, String.class);
//			if (response.getStatusCode() == HttpStatus.OK) {
//				// 解析響應以獲得支付 URL
//				JsonNode rootNode = objectMapper.readTree(response.getBody());
//				String paymentUrl = rootNode.path("info").path("paymentUrl").asText();
//
//			} else {
//
//			}
//
//
//
//		} catch (Exception e) {
//			System.out.println("好像哪裡錯了喔====");
//			e.printStackTrace();
//		}





	}

//	@Bean
//	public ServerEndpointExporter serverEndpointExporter() {
//		return new ServerEndpointExporter();
//	}
//
//	@Override
//	public void onStartup(ServletContext servletContext) throws ServletException {
//		servletContext.setInitParameter("org.apache.tomcat.websocket.textBuffer.Size", "1024000");
//	}

//	public static String postQueryLogisticsTradeInfo(AllInOne all){
//		QueryLogisticsTradeInfoObj obj = new QueryLogisticsTradeInfoObj();
//		obj.setAllPayLogisticsID("2659287");
//		return all.queryLogisticsTradeInfo(obj);
//	}


	private static final String CHANNEL_ID = "2005342190";
	private static final String CHANNEL_SECRET = "44c865afc4d0e1d4575ea90a87616108";
	private static final String REQUEST_URL = "https://sandbox-api-pay.line.me";
	private static final String REQUEST_URI = "/v3/payments/request";

	public static String encrypt(final String keys, final String data) {
		return toBase64String(HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, keys.getBytes()).doFinal(data.getBytes()));
	}

	public static String toBase64String(byte[] bytes) {
		byte[] byteArray = Base64.encodeBase64(bytes);
		return new String(byteArray);
	}

}
