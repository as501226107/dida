package com.dream.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ���ƶ�logo���ƶ������Ķ�ά��
 * 
 * @author songyz
 *
 */
public class ZXingCode {
    private static final int QRCOLOR = 0xFF000000; // Ĭ���Ǻ�ɫ
    private static final int BGWHITE = 0xFFFFFFFF; // ������ɫ

    private static final int WIDTH = 400; // ��ά���
    private static final int HEIGHT = 400; // ��ά���

    // ��������QR��ά�����
    private static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
        private static final long serialVersionUID = 1L;
        {
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// ����QR��ά��ľ�����HΪ��߼��𣩾��弶����Ϣ
            put(EncodeHintType.CHARACTER_SET, "utf-8");// ���ñ��뷽ʽ
            put(EncodeHintType.MARGIN, 0);
        }
    };

    public static void main(String[] args) throws WriterException {
        File logoFile = new File("D://QrCode/ico.png");
        File QrCodeFile = new File("D://QrCode/05.png");
        String url = "https://www.baidu.com/";
        String note = "���ʰٶ�����";
        drawLogoQRCode(logoFile, url, note);
    }

    // ���ɴ�logo�Ķ�ά��ͼƬ
    public static BufferedImage drawLogoQRCode(File logoFile, String qrUrl, String note) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            // ����˳��ֱ�Ϊ���������ݣ��������ͣ�����ͼƬ��ȣ�����ͼƬ�߶ȣ����ò���
            BitMatrix bm = multiFormatWriter.encode(qrUrl, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

            // ��ʼ���ö�ά�����ݴ���BitmapͼƬ���ֱ���Ϊ�ڣ�0xFFFFFFFF���ף�0xFF000000����ɫ
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
                }
            }

            int width = image.getWidth();
            int height = image.getHeight();
            if (Objects.nonNull(logoFile) && logoFile.exists()) {
                // ������ͼ����
                Graphics2D g = image.createGraphics();
                // ��ȡLogoͼƬ
                BufferedImage logo = ImageIO.read(logoFile);
                // ��ʼ����logoͼƬ
                g.drawImage(logo, width * 2 / 5, height * 2 / 5, width * 2 / 10, height * 2 / 10, null);
                g.dispose();
                logo.flush();
            }

            // �Զ����ı�����
            if (StringUtils.isNotEmpty(note)) {
                // �µ�ͼƬ���Ѵ�logo�Ķ�ά�������������
                BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D outg = outImage.createGraphics();
                // ����ά�뵽�µ����
                outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                // �����ֵ��µ����
                outg.setColor(Color.BLACK);
                outg.setFont(new Font("����", Font.BOLD, 30)); // ���塢���͡��ֺ�
                int strWidth = outg.getFontMetrics().stringWidth(note);
                if (strWidth > 399) {
                    // //���ȹ����ͽ�ȡǰ�沿��
                    // ���ȹ����ͻ���
                    String note1 = note.substring(0, note.length() / 2);
                    String note2 = note.substring(note.length() / 2, note.length());
                    int strWidth1 = outg.getFontMetrics().stringWidth(note1);
                    int strWidth2 = outg.getFontMetrics().stringWidth(note2);
                    outg.drawString(note1, 200 - strWidth1 / 2, height + (outImage.getHeight() - height) / 2 + 12);
                    BufferedImage outImage2 = new BufferedImage(400, 485, BufferedImage.TYPE_4BYTE_ABGR);
                    Graphics2D outg2 = outImage2.createGraphics();
                    outg2.drawImage(outImage, 0, 0, outImage.getWidth(), outImage.getHeight(), null);
                    outg2.setColor(Color.BLACK);
                    outg2.setFont(new Font("����", Font.BOLD, 30)); // ���塢���͡��ֺ�
                    outg2.drawString(note2, 200 - strWidth2 / 2,outImage.getHeight() + (outImage2.getHeight() - outImage.getHeight()) / 2 + 5);
                    outg2.dispose();
                    outImage2.flush();
                    outImage = outImage2;
                } else {
                    outg.drawString(note, 200 - strWidth / 2, height + (outImage.getHeight() - height) / 2 + 12); // ������
                }
                outg.dispose();
                outImage.flush();
                image = outImage;
            }
            image.flush();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }

}