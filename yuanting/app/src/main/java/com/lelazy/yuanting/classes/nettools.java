package com.lelazy.yuanting.classes;


import static java.nio.file.Paths.get;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lelazy.yuanting.MainActivity;
import com.lelazy.yuanting.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public  class nettools {
    public  Context netcontext;
    public static String host_ip,host_port;
    public nettools(Context context) {
        this.netcontext=context;
        getipandport();
    }

    void getipandport(){
        this.host_ip=netcontext.getString(R.string.host_ip);
        this.host_port=netcontext.getString(R.string.host_port);
    }
    public static class netmethods{
        int Threadnum=4;
        public static Integer login(Map<String,String> params) throws IOException {
            URL url;
            String uid="",username="",pwd="";
            Integer res;
            for (int i = 0; i < params.size(); i++) {
                uid=params.get("uid");
                username=params.get("username");
                pwd=params.get("pwd");
            }
            url=new URL("http://"+host_ip+":"+host_port+"/user"+"/login"+"?"+"uid="+uid+"&username="+username+"&pwd="+pwd);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            System.out.println(connection.getURL());
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb=new StringBuilder();
            String sss;
            while ((sss=bufferedReader.readLine())!=null){
                sb.append(sss);
            }
            res= Integer.valueOf(sb.toString());
            System.out.println("nettools:"+res.toString());
            return res;
        }
        public static void changename(Integer uid,String newname) throws IOException {
            System.out.println("修改");
            URL url=new URL("http://"+host_ip+":"+host_port+"/user"+"/changemess?"+"uid="+uid+"&name="+newname);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            System.out.println(connection.getResponseCode());
        }
        public static Integer changpwd(Integer uid,String username,String newpwd) throws IOException {
            Integer res;
            URL url=new URL("http://"+host_ip+":"+host_port+"/user"+"/changepwd?"+"uid="+uid+"&username="+username+"&newpwd="+newpwd);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            System.out.println(connection.getURL());
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb=new StringBuilder();
            String sss;
            while ((sss=bufferedReader.readLine())!=null){
                sb.append(sss);
            }
            res= Integer.valueOf(sb.toString());
            System.out.println("nettools:"+res.toString());
            return res;
        }
        public static Integer signup(String username,String pwd) throws IOException {
            Integer uid;
            URL url=new URL("http://"+host_ip+":"+host_port+"/user"+"/signup?"+"name="+username+"&pwd="+pwd);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            System.out.println(connection.getURL());
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb=new StringBuilder();
            String sss;
            while ((sss=bufferedReader.readLine())!=null){
                    sb.append(sss);
                uid= Integer.valueOf(sb.toString());
                System.out.println("nettools:"+uid.toString());
                return uid;
            }
            return -1;
        }
        public static List<Map<String,Integer>> getcoursemess() throws IOException, ClassNotFoundException, JSONException {
            List<Map<String,Integer>> courses= new ArrayList<>();
            URL url=new URL("http://"+host_ip+":"+host_port+"/course/getcoueselist");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            System.out.println(connection.getURL());
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb=new StringBuilder();
            String ss;
            while ((ss=in.readLine())!=null){
                sb.append(ss);
            }
            JSONArray jay=new JSONArray(sb.toString());
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jo= jay.getJSONObject(i);
                Iterator<String> it= jo.keys();
                while (it.hasNext()){
                    String coursekey= it.next();
                    Integer coursevalue= jo.getInt(coursekey);
                    Map<String,Integer> map=new HashMap<>();
                    map.put(coursekey,coursevalue);
                    courses.add(map);
                }
            }
            System.out.println(courses);
            return courses;
        }
        public  static List<pingzi> getpingzilist() throws IOException, JSONException {
            List<pingzi> list=new ArrayList<>();
            URL url=new URL("http://"+host_ip+":"+host_port+"/pingzi/getrpingzi");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            System.out.println(connection.getURL());
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb=new StringBuilder();
            String ss;
            while ((ss=in.readLine())!=null){
                sb.append(ss);
            }
            JSONArray jay=new JSONArray(sb.toString());
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jo= jay.getJSONObject(i);
                String pingziid= jo.getString("pingziid");
                Integer userid=jo.getInt("userid");
                int catagory=jo.getInt("catagory");
                System.out.println("雪豹的json"+jo.getString("pingziid"));
                pingzi x=new pingzi();
                x.setPingziid(pingziid);
                x.setUserid(userid);
                x.setCatagory(catagory);
                list.add(x);
            }
            return list;
        }
        public static List<String> getreshouzhui(pingzi pingzi) throws IOException {
            List<String> houzhui=new ArrayList<>();
            URL url=new URL("http://"+host_ip+":"+host_port+"/pingzi/filehouzhui?userid="+pingzi.getUserid()+"&pingziid="+pingzi.getPingziid());
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            System.out.println(connection.getURL());
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb=new StringBuilder();
            String ss;
            while ((ss=in.readLine())!=null){
                sb.append(ss);
            }
            ss=sb.substring(sb.indexOf("[")+1,sb.indexOf("]"));
            String tupian,yinyue;
            tupian=ss.substring(ss.indexOf("\"")+1,ss.indexOf(",")-1);
            yinyue=ss.substring(ss.indexOf(",")+2,ss.lastIndexOf("\""));
            houzhui.add(tupian);
            houzhui.add(yinyue);
            return houzhui;
        }
//        public static Bitmap getImage(Integer uid,String pingziid,String houzhui) throws Exception {
//            String Url="http://"+host_ip+":"+host_port+"/pingzi/"+uid.toString()+"/"+pingziid+"/"+pingziid+houzhui;
//            Handler handler=new Handler(){
//                @Override
//                public void handleMessage(@NonNull Message msg) {
//                    super.handleMessage(msg);
//                    switch (msg.what){
//                        case 7:
//                            bit=msg.obj;
//                    }
//                }
//            }
//            new  Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        URL url = new URL(Url);
//                        String responseCode = url.openConnection().getHeaderField(0);
//                        if (responseCode.indexOf("200") < 0){
//                           Bitmap  bt= BitmapFactory.decodeStream(url.openStream());
//                           Message message=new Message();
//                           message.obj=bt;
//                           message.what=7;
//                           handler.sendMessage(message);
//
//                        }
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//
//        }
        public static Bitmap getImage(Integer uid,String pingziid,String houzhui) throws Exception {
            String Url="http://"+host_ip+":"+host_port+"/pingzi/"+uid.toString()+"/"+pingziid+"/"+pingziid+houzhui;
            System.out.println(Url);
            try {
                URL url = new URL(Url);
                String responseCode = url.openConnection().getHeaderField(0);

                /*if (responseCode.indexOf("200") < 0)

                    throw new Exception("图片文件不存在或路径错误，错误代码：" + responseCode);*/

                return BitmapFactory.decodeStream(url.openStream());

            } catch (IOException e) {

                // TODO Auto-generated catch block

                throw new Exception(e.getMessage());

            }
        }
        public static pingzi urlgetpingzi(Integer uid,String pingziid) throws IOException, ClassNotFoundException {
            String Url="http://"+host_ip+":"+host_port+"/pingzi/"+uid.toString()+"/"+pingziid+"/"+pingziid+".pingzi";
            URL u=new URL(Url);
            HttpURLConnection connection= (HttpURLConnection) u.openConnection();
            System.out.println(connection.getURL());
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
//            connection.setDoOutput(true);
            connection.connect();
//            java.io.InvalidClassException: com.lelazy.yuanting.classes.pingzi; local class incompatible: stream classdesc serialVersionUID = -6734391319748527360, local class serialVersionUID = 6187649476994780109
            pingzireadstream poread=new pingzireadstream(connection.getInputStream());
//            W/System.err: java.io.InvalidClassException: com.lelazy.yuanting.classes.pingzi; local class incompatible: stream classdesc serialVersionUID = -6734391319748527360, local class serialVersionUID = 6187649476994780109
//            ObjectInputStream poread=new ObjectInputStream(connection.getInputStream());
            pingzi res= (pingzi) poread.readObject();
            return res;
        }
        public static void urlgetmusic(File saveto,Integer uid,String pingziid,String houzhui) throws IOException {
            String url="http://"+host_ip+":"+host_port+"/pingzi/"+uid.toString()+"/"+pingziid+"/"+pingziid+houzhui;
            URL u=new URL(url);
//            downloadmusic[] threads;
            HttpURLConnection connection= (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.addRequestProperty("Accept","audio/*");
            BufferedOutputStream bfo=new BufferedOutputStream(new FileOutputStream(saveto));
            BufferedInputStream ir=new BufferedInputStream(connection.getInputStream());
            int x;
            while ((x=ir.read())!=-1){
                bfo.write(x);
            }
            bfo.flush();
            ir.close();
            bfo.close();

        }
        public static void delpingzi(String pingziid) throws IOException {
            String url = "http://"+host_ip+":"+host_port+"/pingzi/del?id="+pingziid;
            URL uu=new URL(url);
            URLConnection con= uu.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            con.getInputStream();
        }
        public  static void pingziset1(String pingziid) throws IOException {
            String url = "http://"+host_ip+":"+host_port+"/pingzi/set1?id="+pingziid;
            URL uu=new URL(url);
            URLConnection con= uu.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            con.getInputStream();
        }
        public static void pingziset0(String pingziid) throws IOException {
            String url = "http://"+host_ip+":"+host_port+"/pingzi/set0?id="+pingziid;
            URL uu=new URL(url);
            URLConnection con= uu.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            con.getInputStream();
        }
        public static void pingzilistset1(List<pingzi> list) throws Exception {
            String url = "http://"+host_ip+":"+host_port+"/pingzi/s1eil?list="+list;
            URL uu=new URL(url);
            URLConnection con= uu.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            con.getInputStream();
        }
        public static void uploadfiles(File file) throws IOException {
            String boundary="---lelazy---";
            Integer uid= MainActivity.currentuser.getUid();
            URL url=new URL("http://"+host_ip+":"+host_port+"/uploadfile?uid="+uid+"&filename="+file.getName()+"&file="+file);
            StringBuilder sb = new StringBuilder();
            sb.append("--" + boundary + "\r\n");
            sb.append("Content-Disposition: form-data; name=\"uid\"" + "\r\n");
            sb.append("\r\n");
            sb.append(uid.toString() + "\r\n");
            sb.append("--" + boundary + "\r\n");
/*            sb.append("Content-Disposition: form-data; name=\"filename\"" + "\r\n");
            sb.append("\r\n");
            sb.append(file.getName() + "\r\n");
            sb.append("--" + boundary + "\r\n");*/
            sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getAbsolutePath() + "\"" + "\r\n");
            System.out.println("此时文件："+file.getName());
            sb.append("Content-Type: application/octet-stream" + "\r\n");
            sb.append("\r\n");
            // 将开头和结尾部分转为字节数组，因为设置Content-Type时长度是字节长度
            byte[] before = sb.toString().getBytes("UTF-8");
            byte[] after = ("\r\n--" + boundary + "--\r\n").getBytes("UTF-8");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(3000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setRequestProperty("Content-Length", before.length + file.length() + after.length + "");
            conn.setRequestProperty("HOST", host_ip + ":" + host_port);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
// 将开头部分写出
            out.write(before);
// 写出文件数据
            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) != -1)
                out.write(buf, 0, len);
// 将结尾部分写出
            out.write(after);
            fis.close();
            out.close();
            System.out.println(conn.getResponseCode());
        }
       public static void uploadsingle(String id,String name,File file) throws Exception {
           String boundary="---lelazy---";
           URL url=new URL("http://"+host_ip+":"+host_port+"/uploadfile?uid="+id+"&filename="+name+"&file="+file);
           StringBuilder sb = new StringBuilder();
           sb.append("--" + boundary + "\r\n");
           sb.append("Content-Disposition: form-data; name=\"uid\"" + "\r\n");
           sb.append("\r\n");
           sb.append(id.toString() + "\r\n");
           sb.append("--" + boundary + "\r\n");
/*            sb.append("Content-Disposition: form-data; name=\"filename\"" + "\r\n");
            sb.append("\r\n");
            sb.append(file.getName() + "\r\n");
            sb.append("--" + boundary + "\r\n");*/
           sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getAbsolutePath() + "\"" + "\r\n");
           System.out.println("此时文件："+file.getName());
           sb.append("Content-Type: application/octet-stream" + "\r\n");
           sb.append("\r\n");
           // 将开头和结尾部分转为字节数组，因为设置Content-Type时长度是字节长度
           byte[] before = sb.toString().getBytes("UTF-8");
           byte[] after = ("\r\n--" + boundary + "--\r\n").getBytes("UTF-8");
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("POST");
           conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
           conn.setRequestProperty("Content-Length", before.length + file.length() + after.length + "");
           conn.setRequestProperty("HOST", host_ip + ":" + host_port);
           conn.setDoOutput(true);
           OutputStream out = conn.getOutputStream();
           FileInputStream fis = new FileInputStream(file);
// 将开头部分写出
           out.write(before);
// 写出文件数据
           byte[] buf = new byte[1024];
           int len;
           while ((len = fis.read(buf)) != -1)
               out.write(buf, 0, len);
// 将结尾部分写出
           out.write(after);
           fis.close();
           out.close();
           System.out.println(conn.getResponseCode());
       }
    }


}
