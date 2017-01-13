package com.kunzhuo.xuechelang.utils;

import android.text.TextUtils;
import android.widget.TextView;

import com.ta.utdid2.android.utils.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by waaa on 2016/9/1.
 * 工具类
 */
public class ToolsUtils {

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        // String telRegex = "[1][3456789]\\d{9}";//
        // "[1]"代表第1位为数字1，"[3456789]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }


    // 验证密码格式
    public static boolean checkPasswdLegality(String passwd) {
        String regex = "[0-9a-zA-Z,._?/~!@#$%^&*()+=\\|{}]{5,17}$";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(passwd);

        return m.matches();
    }

    public static String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    /**
     * 保存list转String工具
     */
    public static String SceneList2String(List SceneList) throws IOException {

        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }

    @SuppressWarnings({"rawtypes"})
    /**
     * String 转List
     */
    public static List String2SceneList(String SceneListString)
            throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List SceneList = (List) objectInputStream.readObject();

        objectInputStream.close();

        return SceneList;
    }

    private static int fromTextView(TextView view) {

        int num = Integer.parseInt(view.getText().toString());

        return num;

    }

    public static int setRandom(int num) {

        Random random = new Random();

        int a = random.nextInt(num);

        if (a == 0) {
            return a + 1;
        } else
            return a;

    }


    public static int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    /**
     * 生成a位随机数
     *
     * @return
     */
    public static int getSixRandom(int a) {

        int numcode = (int) ((Math.random() * 9 + 1) * 10 * a);
        return numcode;

    }
}
