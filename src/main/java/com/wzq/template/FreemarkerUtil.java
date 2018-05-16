package com.wzq.template;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

/**
 * freemarker填充工具类
 */
public class FreemarkerUtil {

    private String basePackagePath;
    private Version version;
    private String encoding;

    public FreemarkerUtil(String basePackagePath, Version version, String encoding) {
        this.basePackagePath = basePackagePath;
        this.version = version;
        this.encoding = encoding;
    }

    public Template getTemplate(String tplName){
        try {
            Configuration configuration = null;
            if(version != null){
                try {
                    configuration = Configuration.class.getConstructor(Version.class).newInstance(version);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
               if(configuration == null){
                    configuration = new Configuration();
               }
            }else{
                configuration = new Configuration();
            }
            configuration.setDefaultEncoding(encoding);
            configuration.setDirectoryForTemplateLoading(new File(basePackagePath));

            Template template = configuration.getTemplate(tplName);

            return template;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void print(String tplName, Map<String, Object> root, OutputStream os){
        Template template = this.getTemplate(tplName);
        try {
            template.process(root, new PrintWriter(os));//在控制台输出内容
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String print(String tplName, Map<String, Object> root) throws UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        print(tplName, root, baos);
        return new String(baos.toByteArray(), encoding);
    }

    public void print(String tplName, Map<String, Object> root, String outFilePath) throws IOException {
        print(tplName, root, new FileOutputStream(createNotExistsFile(outFilePath)));
    }

    private File createNotExistsFile(String filePath) throws IOException {
        File file = new File(filePath);
        if(!file.exists()){
            File parentFile = file.getParentFile();
            if(!parentFile.exists()){
                parentFile.mkdirs();
            }
            file.createNewFile();
        }
        return file;
    }
}