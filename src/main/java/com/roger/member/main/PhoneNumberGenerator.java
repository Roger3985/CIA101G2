package com.roger.member.main;

import com.roger.member.service.MemberService;
import com.roger.member.service.impl.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

public class PhoneNumberGenerator {

    // 生成以 "09" 開頭的隨機電話號碼
    public static String generatePhoneNumber() {

        Random random = new Random();
        StringBuilder phoneNumber = new StringBuilder("09");

        // 生成後面的的 8 位數字
        for (int i = 0; i < 8; i++) {
            phoneNumber.append(random.nextInt(10));
        }

        return phoneNumber.toString();
    }

    public static void main(String[] args) {
        // 要生成的電話號碼數量
        int numberOfPhoneNumbers = 10;

        // 生成指定數量的電話號碼並輸出
        System.out.println("Generated phone numbers:");
        for (int i = 0; i < numberOfPhoneNumbers; i++) {
            System.out.println(generatePhoneNumber());
        }
    }
}
