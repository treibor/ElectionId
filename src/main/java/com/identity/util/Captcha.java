package com.identity.util;

import java.awt.image.BufferedImage;

import com.vaadin.flow.component.html.Image;

public interface Captcha {
    BufferedImage getCaptchaBufferedImage();
    boolean checkUserAnswer(String userAnswer);
    Image getCaptchaImg();
}