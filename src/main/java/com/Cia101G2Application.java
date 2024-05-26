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

}
