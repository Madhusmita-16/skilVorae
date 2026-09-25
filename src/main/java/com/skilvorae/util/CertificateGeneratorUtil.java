package com.skilvorae.util;

import com.skilvorae.dto.CertificateDto;

/**
 * Utility for rendering HTML certificate layout templates for student course graduation.
 */
public final class CertificateGeneratorUtil {

    private CertificateGeneratorUtil() {
        // Private constructor
    }

    /**
     * Renders HTML template for printable SkilVorae course completion certificate.
     *
     * @param cert Certificate DTO payload.
     * @return HTML formatted certificate string.
     */
    public static String renderCertificateHtml(CertificateDto cert) {
        if (cert == null) return "<html><body><h1>Invalid Certificate</h1></body></html>";

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><style>");
        sb.append("body { font-family: 'Georgia', serif; text-align: center; color: #1e293b; background: #f8fafc; padding: 40px; }");
        sb.append(".cert-card { border: 12px double #0284c7; background: #ffffff; padding: 60px; max-width: 800px; margin: 0 auto; box-shadow: 0 10px 25px rgba(0,0,0,0.1); }");
        sb.append(".title { font-size: 32px; color: #0369a1; text-transform: uppercase; font-weight: bold; letter-spacing: 2px; }");
        sb.append(".sub { font-size: 16px; color: #64748b; margin-top: 12px; }");
        sb.append(".recipient { font-size: 28px; font-weight: bold; color: #0f172a; margin: 24px 0; border-bottom: 2px solid #cbd5e1; display: inline-block; padding: 0 30px 8px 30px; }");
        sb.append(".course { font-size: 22px; color: #0284c7; font-weight: bold; }");
        sb.append(".code { font-family: monospace; font-size: 14px; color: #94a3b8; margin-top: 40px; }");
        sb.append("</style></head><body>");

        sb.append("<div class='cert-card'>");
        sb.append("<div class='title'>Certificate of Completion</div>");
        sb.append("<div class='sub'>This is proudly presented to</div>");
        sb.append("<div class='recipient'>").append(cert.getRecipientName()).append("</div>");
        sb.append("<div class='sub'>for successfully completing the online course</div>");
        sb.append("<div class='course'>").append(cert.getCourseTitle()).append("</div>");
        sb.append("<div class='sub' style='margin-top:20px;'>Instructed by: ").append(cert.getInstructorName()).append("</div>");
        sb.append("<div class='code'>Certificate ID: ").append(cert.getCertificateCode()).append("</div>");
        sb.append("</div></body></html>");

        return sb.toString();
    }
}
