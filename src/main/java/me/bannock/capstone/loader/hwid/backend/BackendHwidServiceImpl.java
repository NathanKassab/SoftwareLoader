package me.bannock.capstone.loader.hwid.backend;

import me.bannock.capstone.loader.hwid.HwidService;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GraphicsCard;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BackendHwidServiceImpl implements HwidService {

    @Override
    public String getHwid() {
        StringBuilder hwidBuilder = new StringBuilder();

        SystemInfo systemInfo = new SystemInfo();
        for (GraphicsCard graphicsCard : systemInfo.getHardware().getGraphicsCards()) {
            hwidBuilder.append(graphicsCard.getDeviceId()).append(graphicsCard.getName()).append(graphicsCard.getVendor()).append(graphicsCard.getVRam());
        }
        CentralProcessor.ProcessorIdentifier processorIdentifier = systemInfo.getHardware().getProcessor().getProcessorIdentifier();
        hwidBuilder.append(processorIdentifier.getFamily()).append(processorIdentifier.getIdentifier()).append(processorIdentifier.getVendor()).append(processorIdentifier.getMicroarchitecture());

        String hwid;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(hwidBuilder.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            byte[] byteData = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            hwid = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }

        return hwid;
    }

}
