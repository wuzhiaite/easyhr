package com.springboot.easyhr.common.util;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.util.StringUtils;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * word文件导出工具
 */
public class PoiWordUtil {


    /**
     *
     * @param cell
     * @param width
     * @param typeEnum
     * @param align
     */
    public static void setCellWitchAndAlign(XWPFTableCell cell, String width, STVerticalJc.Enum typeEnum, STJc.Enum align){
        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(typeEnum);
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(align);
        CTTblWidth ctTblWidth = (ctPr != null && ctPr.isSetTcW() && ctPr.getTcW()!=null &&ctPr.getTcW().getW()!=null) ? ctPr.getTcW(): ctPr.addNewTcW();
        if(!StringUtils.isEmpty(width)){
            ctTblWidth.setW(new BigInteger(width));
            ctTblWidth.setType(STTblWidth.DXA);

        }
    }

    /**
     * 生成word文档下载
     */
    public static void downloadWord(HttpServletResponse response,Map<String, Object> map, String file){
        XWPFDocument document = null;
        ServletOutputStream servletOS = null;
        ByteArrayOutputStream ostream = null;
        //添加表格
        try {
            servletOS = response.getOutputStream();
            ostream = new ByteArrayOutputStream();
            document = new XWPFDocument(POIXMLDocument.openPackage(file));// 生成word文档并读取模板
            replaceTableLables(document,map);//替换表格中的数据
            replaceParagraphyLables(document,map);//替换段落数据
            replaceTextToImage(document,map);//替换图片


            //输出word内容文件流，提供下载
            response.setContentType("application/x-msdownload");
            String name = java.net.URLEncoder.encode("生成word.docx", "UTF8");
            name = new String((name).getBytes("UTF-8"), "ISO-8859-1");
            response.addHeader("Content-Disposition", "attachment; filename*=utf-8'zh_cn'"+name);
            document.write(ostream);
            servletOS.write(ostream.toByteArray());
        } catch (Exception e1) {
            e1.printStackTrace();
        }finally{
            try{
                if(ostream != null){
                    ostream.close();
                }
                if(servletOS != null){
                    servletOS.close();
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private static void replaceTextToImage(XWPFDocument document, Map<String, Object> map) {


        List listRun;
        List<XWPFParagraph> listParagraphs = document.getParagraphs();
        for (int sa = 0; sa < listParagraphs.size(); sa++) {
            String text = listParagraphs.get(sa).getText();
            //对参数进行过滤，判断是否为{{}}包裹的数据，
            String alterNativeText  = getAlternative(text);
            alterNativeText = alterNativeText.trim();
            String[] split = alterNativeText.split("_");
            if( split.length <4 || !split[1].trim().equals("IMAGE")){
                continue;
            }
            String value = getValueByKey(map,alterNativeText);
            value = StringUtils.isEmpty(value) ? "  " : value ;
            listRun = listParagraphs.get(sa).getRuns();
            for (int p = 0; p < listRun.size(); p++) {
                if (listRun.get(p).toString().equals(alterNativeText)) {
                    listParagraphs.get(sa).removeRun(p);//移除占位符
                    //获得当前CTInline
                    CTInline inline = listParagraphs.get(sa).createRun().getCTR().addNewDrawing().addNewInline();
                    try {
                        insertPicture(document,value,inline,Integer.parseInt(split[2]),Integer.parseInt(split[3]));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

        }



    }

    /**
     * 图片插入操作
     *
     */
    private static void insertPicture(XWPFDocument document,String filePath,CTInline inline,int width, int height) {
        try {
            String ind = document.addPictureData(new FileInputStream(filePath), 5);
            int id = document.getAllPictures().size()-1;
            final int EMU = 9525;
            width *= EMU;
            height *= EMU;
            String blipId = document.getAllPictures().get(id).getPackageRelationship()
                    .getId();
            String picXml = ""
                    + ""
                    + "   "
                    + "      "
                    + "         "
                    + id
                    + "\" name=\"Generated\"/>"
                    + "            "
                    + "         "
                    + "         "
                    + "       "
                    + blipId
                    + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
                    + "            "
                    + "               "
                    + "            "
                    + "         "
                    + "         "
                    + "            "
                    + "               "
                    + "    "
                    + width
                    + "\" cy=\""
                    + height
                    + "\"/>"
                    + "            "
                    + "            "
                    + "               "
                    + "            "
                    + "         "
                    + "      "
                    + "   " + "";
            inline.addNewGraphic().addNewGraphicData();
            XmlToken xmlToken = null;
            try {
                xmlToken = XmlToken.Factory.parse(picXml);
            } catch (Exception xe) {
                xe.printStackTrace();
            }
            inline.set(xmlToken);
            inline.setDistT(0);
            inline.setDistB(0);
            inline.setDistL(0);
            inline.setDistR(0);
            CTPositiveSize2D extent = inline.addNewExtent();
            extent.setCx(width);
            extent.setCy(height);
            CTNonVisualDrawingProps docPr = inline.addNewDocPr();
            docPr.setId(id);
            docPr.setName("IMG_" + id);
            docPr.setDescr("IMG_" + id);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * 替换段落标签
     * @param document
     * @param map
     */
    private static void replaceParagraphyLables(XWPFDocument document, Map<String, Object> map) {





    }

    public static void replaceTableLables( XWPFDocument document,Map<String, Object> map){
        Iterator<XWPFTable> it = document.getTablesIterator();
        //表格内容替换添加
        while(it.hasNext()){
            XWPFTable table = it.next();
            int rcount = table.getNumberOfRows();
            for(int i =0 ;i < rcount;i++){
                XWPFTableRow row = table.getRow(i);
                List<XWPFTableCell> cells =  row.getTableCells();
                for (XWPFTableCell cell : cells){
                    String cellText = cell.getText();
                    //对参数进行过滤，判断是否为{{}}包裹的数据，
                    String alterNativeText  = getAlternative(cellText);
                    if(!StringUtils.isEmpty(alterNativeText)){
                        //删除原来内容
                        cell.removeParagraph(0);
                        String value = getValueByKey(map,alterNativeText.trim());
                        value = StringUtils.isEmpty(value) ? "  " : value ;
                        String[] split = value.split("_");
                        IFactory factory = new PoiWordUtil().new LableTextFactory();
                        if(split.length > 1){
                            switch(split[1]){
                                case "DATE":factory = new PoiWordUtil().new LableDateFactory(); break;
                                case "CHILDTABLE":factory = new PoiWordUtil().new ChildTableLable();break;
                            }
                        }
                        ChangeLable  changeLable = factory.createChangeLable();
                        changeLable.reverseValue(split,cell,value);


                    }
                }
            }
        }
    }


    /**
     * 根据key获取value值
     * @param map
     * @param text
     * @return
     */
    private static String getValueByKey(Map<String, Object> map, String text) {
        if( StringUtils.isEmpty(text))return "";
        String[] split = text.split("_");
        if(split.length > 1){
            return (String) map.get(split[1]);
        }
        return (String)map.get(text);
    }

    /**
     * 获取特殊字符的处理
     * @param cellText
     * @return
     */
    private static String getAlternative(String cellText) {
        if(StringUtils.isEmpty(cellText)) return "";
        int startIdx = cellText.indexOf("{{");
        int endIdx = cellText.indexOf("}}");
        if(startIdx == -1 || endIdx == -1)return cellText;
        cellText = cellText.substring(startIdx+2,endIdx);
        return cellText;
    }

    public static void  generateFile(Map<String,String> column ,List<String> data){

    }
    //工厂类接口
    interface IFactory{
        ChangeLable createChangeLable();
    }
    interface ChangeLable{
        void reverseValue(String[] split, XWPFTableCell cell, String value);
    }

    abstract class ChangeLableDecorate implements ChangeLable{

      public void   reverseLableToText( XWPFTableCell cell,String[] split, String value){
          //写入新内容
          String cellText = cell.getText();
          String alterNativeText = StringUtil.join(split, "_");
          cellText.replace("{{"+alterNativeText+"}}",value);
          cell.setText(cellText);
        }

    }

    /**
     * 数据表单替换
     */
    private  class ChangeDateLable extends ChangeLableDecorate {
        private static final String DEFAULT_DATE_FORMATE = "yyyy-MM-dd";
        private SimpleDateFormat sdf ;

        @Override
        public void reverseValue(String[] split, XWPFTableCell cell, String value) {
            if(StringUtils.isEmpty(split[2])){
                sdf = new SimpleDateFormat(DEFAULT_DATE_FORMATE);
            }else{
                sdf = new SimpleDateFormat(split[2].trim());
            }
            String formatValue = sdf.format(split[0]);
            this.reverseLableToText(cell,split,formatValue);

        }
    }

    /**
     * 创捷日期格式化类对象
     */
    private  class LableDateFactory implements IFactory {

        @Override
        public ChangeLable createChangeLable() {
            return new ChangeDateLable();
        }
    }


    private   class ChildTableLable implements IFactory {
        @Override
        public ChangeLable createChangeLable() {
            return null;
        }
    }

    /**
     * 创建文本替换类
     */
    private  class LableTextFactory implements IFactory {
        @Override
        public ChangeLable createChangeLable() {
            return new ChangeTextLable();
        }
    }

    /**
     * 文本替换
     */
    private class ChangeTextLable extends ChangeLableDecorate {
        @Override
        public void reverseValue(String[] split, XWPFTableCell cell, String value) {
            this.reverseLableToText(cell,split,value);
        }
    }

}
