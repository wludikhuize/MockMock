package com.mockmock.htmlbuilder;

import com.mockmock.AppStarter;
import com.mockmock.Settings;
import com.mockmock.Util;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class FooterHtmlBuilder implements HtmlBuilder {
    private Settings settings;

    @Autowired
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String build() {
        String output = "";
        Util util = new Util();

        if (settings.getStaticFolderPath() != null) {
            output += "  <script src=\"/js/jquery-1.8.1.min.js\"></script>\n";
            output += "  <script src=\"/js/bootstrap.min.js\"></script>\n";
            output += "  <script src=\"/js/cookies.js\"></script>\n";
            output += "  <script src=\"/js/mockmock.js\"></script>\n";
            output += "  <script src=\"/js/clipboard.min.js\"></script>\n";
        } else {
            output += "  <script>\n" + util.getFile("/js/jquery-1.8.1.min.js") + "</script>\n" + "  <script>\n";
            output += util.getFile("/js/bootstrap.min.js") + "</script>\n" + "  <script>\n";
            output += util.getFile("/js/cookies.js") + "</script>\n" + "  <script>\n";
            output += util.getFile("/js/mockmock.js") + "</script>\n" + "  <script>\n";
            output += util.getFile("/js/clipboard.min.js") + "</script>\n";
        }

        output += " <script>\n" + "   new ClipboardJS('.btn');\n" + " </script>\n";

        output += "  </body>\n" + "</html>\n";

        return output;
    }
}
