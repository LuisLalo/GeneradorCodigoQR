/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uabc.generadorcodigoqr;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author UABC-16653387
 */
public class CodigoQR {
    
    public static void main(String[] args) {
        
        CodigoQR qr = new CodigoQR();
        File archivo = new File("CodigoQR.png");
        String texto = "?re=UAE5702287S5&rr=CAEL850102LQ8&tt=0000004761.150000&id=984D1256-72C1-4B83-A989-62E924ACD4ED";
        //String texto = "?re=UAE5702287S5&RR=CAEL850102LQ8&TT=0000004761.150000&id=984D1256-72C1-4B83-A989-62E924ACD4ED";
        
        try {
            
            qr.generarQR(archivo, texto, 300, 300);
            System.out.println("Código QR generado: " + archivo.getAbsolutePath());
            
            String contenidoQR = qr.decodificaQR(archivo);
            System.out.println("Texto: " + contenidoQR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public File generarQR(File archivo, String texto, int alto, int ancho) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(texto, com.google.zxing.BarcodeFormat.QR_CODE, alto, ancho);

        BufferedImage image = new BufferedImage(matrix.getWidth(), matrix.getHeight(), BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrix.getWidth(), matrix.getHeight());
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrix.getWidth(); i++) {
            for (int j = 0; j < matrix.getHeight(); j++) {
                if (matrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }

        ImageIO.write(image, "png", archivo);

        return archivo;
    }
    
    public String decodificaQR(File file) throws Exception {

        FileInputStream inputStream = new FileInputStream(file);

        BufferedImage image = ImageIO.read(inputStream);

        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];

        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        // decode the barcode
        QRCodeReader reader = new QRCodeReader();
        Result result = reader.decode(bitmap);
        return new String(result.getText());
    }
}