package com.springboot.easyhr.common.util;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.util.BytePictureUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PoiTest {

    public void testNumbericRender() throws Exception {
        Map<String, Object> datas = new HashMap<String, Object>() {
            {
                //本地图片
                put("localPicture", new PictureRenderData(100, 120, "src/test/resources/logo.png"));
                //网路图片
                put("urlPicture", new PictureRenderData(100, 100, ".png", BytePictureUtils.getUrlByteArray("https://avatars3.githubusercontent.com/u/1394854?v=3&s=40")));
            }
        };

        XWPFTemplate template = XWPFTemplate.compile("~/picture.docx")
                .render(datas);

        FileOutputStream out = new FileOutputStream("out_picture.docx");
        template.write(out);
        out.flush();
        out.close();
        template.close();
    }
}
