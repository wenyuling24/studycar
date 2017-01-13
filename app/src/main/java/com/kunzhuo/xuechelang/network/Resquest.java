package com.kunzhuo.xuechelang.network;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.baidu.platform.comapi.map.N;
import com.kunzhuo.xuechelang.http.AsyncHttpResponseHandler;
import com.kunzhuo.xuechelang.http.HttpClientUtils;
import com.kunzhuo.xuechelang.http.HttpParams;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by waaa on 2016/8/31.
 */
public class Resquest {

//    文档地址 http://api.xclang.com/Default.aspx
//    接口地址 http://api.xclang.com
//    用户名：接口名
//    密码：C741E1B01813BE8B93D4EE874A24412B
//    签名 接口名+密码 32位MD5

    //    // 接口地址服务器
    public static final String SERVER_ADDRESS = "http://api.xclang.com/"; //
    public static final String PIC_ADDRESS = "http://api.xclang.com/PictureUpload.ashx?";


    // 接口地址本地
//    public static final String SERVER_ADDRESS = "http://192.168.0.195:1541/"; //
//    public static final String PIC_ADDRESS = "http://192.168.0.195:1541/PictureUpload.ashx?";

    // +加密方式
    public static final String SIGNKEY = "C741E1B01813BE8B93D4EE874A24412B";
    public static final String POST = "xcLangServer.aspx";

    // 发送短信方法名
    public static final String method_SendSMS = "SendSMS";

    // 用户注册
    public static final String method_UserRegs = "UserRegs";

    //理论章节 ChapterList
    public static final String method_ChapterList = "ChapterList";

    // 题目
    public static final String method_TitleList_KM = "TitleList_KM";

    //答案 TitleAnswerList
    public static final String method_TitleAnswerList = "TitleAnswerList";

    //账号密码登录
    public static final String method_UserLogin = "UserLogin";

    // 验证码登录
    public static final String method_UserInform = "UserInform";

    // 忘记密码 UserForgetPwd
    public static final String method_UserForgetPwd = "UserForgetPwd";

    // 题目收藏 ColnTopicAdd
    public static final String method_ColnTopicAdd = "ColnTopicAdd";

    // 取消收藏 ColnTopicDel
    public static final String method_ColnTopicDel = "ColnTopicDel";

    // 查看收藏 ColnTopicSel
    public static final String method_ColnTopicSel = "ColnTopicSel";

    //主页图集 CarouselList
    public static final String method_CarouselList = "CarouselList";

    // 首页数量
    public static final String method_OneSubjectCount = "OneSubjectCount";

    // 题目记录
    public static final String method_Record = "Record";

    // 添加错题
    public static final String method_TitleError_Add = "TitleError_Add";

    // 消除错题
    public static final String method_TitleError_Del = "TitleError_Del";

    // 错题列表
    public static final String method_TitleError_List = "TitleError_List";

    //模拟考试
    public static final String method_TitleRecord_MNKS = "TitleRecord_MNKS";

    // 提交答题记录
    public static final String method_TitleRecord_Edit = "TitleRecord_Edit";

    // 排行、科目添加 MnksRanking
    public static final String method_MnksRanking = "MnksRanking";

    // 获取考试记录
    public static final String method_MnksRankingJL = "MnksRankingJL";

    // 获取排行榜
    public static final String method_MnksRankingPX = "MnksRankingPX";

    // 视频列表 VideoList
    public static final String method_VideoList = "VideoList";

    // 文章列表 NoviceList
    public static final String method_NoviceList = "NoviceList";

    // 文章详情 NoviceListID
    public static final String method_NoviceListID = "NoviceListID";

    // 视频详情
    public static final String method_VideoDetails = "VideoDetails";

    // 点赞
    public static final String method_ThingAdd = "ThingAdd";

    // 取消点赞
    public static final String method_ThingDelete = "ThingDelete";

    // 视频收藏添加
    public static final String method_CollectionAdd = "CollectionAdd";

    //  取消收藏
    public static final String method_CollectionDelete = "CollectionDelete";

    // 视频点击量
    public static final String method_Vide_Edit_Playnumber = "Vide_Edit_Playnumber";

    // 获取评论列表
    public static final String methmethod_CommentList = "CommentList";

    // 添加评论
    public static final String method_CommentAdd = "CommentAdd";

    // 微信登录
    public static final String method_UserLogin_WeChat = "UserLogin_WeChat";

    // 获取视频分类
    public static final String method_VideoType = "VideoType";

    // 获取视频分类下单视频列表
    public static final String method_VideoListType = "VideoListType";

    // 获取当前用户周围3公里用户 MapList
    public static final String method_MapList = "MapList";

    // 当前用户周围3公里用户 MapListUser(有距离数据)
    public static final String method_MapListUser = "MapListUser";

    // 微网站个人信息 MapUser
    public static final String method_MapUser = "MapUser";

    // 微网站个人相册 MapUserPic
    public static final String method_MapUserPic = "MapUserPic";

    // 获取驾校列表
    public static final String method_User_school = "User_school";

    // 微网站评论列表 MapCommentList
    public static final String method_MapCommentList = "MapCommentList";

    // 绑定微信 UserWeChatAdd
    public static final String method_UserWeChatAdd = "UserWeChatAdd";

    // 微网站用户增加 Extension_User_Add
    public static final String method_Extension_User_Add = "Extension_User_Add";

    // 微网站用户相册上传 MapUserPicAdd
    public static final String method_MapUserPicAdd = "MapUserPicAdd";

    // 判断用户登录或绑定微信 WeChatJudgment
    public static final String method_WeChatJudgment = "WeChatJudgment";

    // 绑定微信(未注册用户系统自动注册) UserWeChat_UaccountAdd
    public static final String method_UserWeChat_UaccountAdd = "UserWeChat_UaccountAdd";

    //补充用户资料 UserSystemEidt
    public static final String method_UserSystemEidt = "UserSystemEidt";

    // 微网站用户相册删除 MapUserPicDel
    public static final String method_MapUserPicDel = "MapUserPicDel";

    // 微网站用户修改 Extension_User_Edit
    public static final String method_Extension_User_Edit = "Extension_User_Edit";

    // openid查找个人信息 MapUser_I
    public static final String method_MapUser_I = "MapUser_I";

    // 教练视频列表CoachVideoList
    public static final String method_CoachVideoList = "CoachVideoList";

    //  教练列表 CoachList
    public static final String method_CoachList = "CoachList";

    //单教练信息 CoachSingle
    public static final String method_CoachSingle = "CoachSingle";

    // 获取区域信息
    public static final String method_RegionList = "RegionList";

    //微网站用户评论 MapCommentAdd
    public static final String method_MapCommentAdd = "MapCommentAdd";

    // 获取特定语音列表
    public static final String method_ArticleFixedList = "VoiceList";

    // 获取特定灯光列表
    public static final String method_Lighting = "Lighting";

    // 模糊查询教练 Coach_Title
    public static final String method_Coach_Title = "Coach_Title";

    // 模糊查询视频 Video_Title
    public static final String method_Video_Title = "Video_Title";

    // 陪练商品列表
    public static final String method_SparringList = "SparringList";

    //陪练商品详情 SparringSingle
    public static final String method_SparringSingle = "SparringSingle";

    //陪练商品单价列表 UnitPriceList
    public static final String method_UnitPriceList = "UnitPriceList";

    //  陪练信息添加 Sp_ordered_Add
    public static final String method_Sp_ordered_Add = "Sp_ordered_Add";

    //陪练信息添加 Sp_ordered_Add_nT
    public static final String method_Sp_ordered_Add_nT = "Sp_ordered_Add_nT";

    // 教练陪练订单 SpOrders_Coach
    public static final String method_SpOrders_Coach = "SpOrders_Coach";

    // 我的陪练订单 SpOrdered_I
    public static final String method_SpOrdered_I = "SpOrdered_I";

    // 试学 TryTolearn_Add
    public static final String method_TryTolearn_Add = "TryTolearn_Add";

    // 试驾 TestDrive_Add
    public static final String method_TestDrive_Add = "TestDrive_Add";

    // 我的视频收藏 Collection_Sel
    public static final String method_Collection_Sel = "Collection_Sel";

    //  教练认证附件添加 Coachfile_Add
    public static final String method_Coachfile_Add = "Coachfile_Add";

    // 教练认证附件查看 Coachfile_Sel
    public static final String method_Coachfile_Sel = "Coachfile_Sel";

    // 用户个人信息 UserInformation
    public static final String method_UserInformation = "UserInformation";

    // 教练查看自己的试学 TryTolearn_ListDB
    public static final String method_TryTolearn_ListDB = "TryTolearn_ListDB";

    //教练试学状态修改 TryTolearn_EditState
    public static final String method_TryTolearn_EditState = "TryTolearn_EditState";

    // 下订单 CouponTabAdd
    public static final String method_CouponTabAdd = "CouponTabAdd";

    // 汽车类型
    public static final String method_ParameterUsed = "ParameterUsed";

    // 二手车列表 UsedCarList
    public static final String method_UsedCarList = "UsedCarList";

    //  二手车详情 UsedCarTab_I
    public static final String method_UsedCarTab_I = "UsedCarTab_I";

    // 二手车图片
    public static final String method_UsedCarPicList = "UsedCarPicList";

    //  砍价预约 E_UsedCarBargainAdd
    public static final String method_E_UsedCarBargainAdd = "E_UsedCarBargainAdd";


    // 是否成功标志位
    public static final int FAILED_MSG = 111111;
    public static final int SUCCESS_MSG = 000000;


    /**
     * 签名生成算法
     *
     * @param <String,String> params 请求参数集，所有参数必须已转换为字符串类型
     * @param secret          签名密钥
     * @return 签名HttpPost
     * @throws IOException
     */
    @SuppressLint("NewApi")
    public static String getSignature(HashMap<String, String> params,
                                      String secret) throws IOException {

        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<String, String>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();

        for (Map.Entry<String, String> param : entrys) {
            basestring.append(param.getValue());
        }

        basestring.append(secret);
        System.out.println(basestring + " 签名加密之前");
        //
        // // 遍历排序后的字典，将所有参数按"value"格式拼接在一起
        // StringBuilder basestring2 = new StringBuilder();
        //
        // for (Entry<String, String> param : entrys) {
        // basestring2.append("&" + param.getKey() + "=" + param.getValue());
        // }
        // basestring2.append(secret);
        // System.out.println(basestring2 + " 签名加密之前2");

        // 使用MD5对待签名串求签
        byte[] bytes = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
        } catch (GeneralSecurityException ex) {
            throw new IOException(ex);
        }

        // 将MD5输出的二进制结果转换为小写的十六进制
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex);
        }
        return sign.toString();
    }

    // 砍价预约 E_UsedCarBargainAdd
    public static HashMap<String, String> getHashMap82(String Car_Name, String Car_Telephone, String Car_Amount, String Car_UsedID) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_E_UsedCarBargainAdd);
        map.put("Car_Name", Car_Name);
        map.put("Car_Telephone", Car_Telephone);
        map.put("Car_Amount", Car_Amount);
        map.put("Car_UsedID", Car_UsedID);
        return map;
    }

    //二手车图片 UsedCarTab_I
    public static HashMap<String, String> getHashMap81(String Car_UsedID) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_UsedCarPicList);
        map.put("Car_UsedID", Car_UsedID);
        return map;
    }


    // 二手车详情 UsedCarTab_I
    public static HashMap<String, String> getHashMap80(String Car_UsedID) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_UsedCarTab_I);
        map.put("Car_UsedID", Car_UsedID);
        return map;
    }

    // 二手车列表 UsedCarList
    public static HashMap<String, String> getHashMap79(String Brand, String Models, int pageNumber, int pageNum) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_UsedCarList);
        map.put("Brand", Brand);
        map.put("Models", Models);
        map.put("pageNumber", pageNumber + "");
        map.put("pageNum", pageNum + "");
        return map;
    }


    // 汽车类型
    public static HashMap<String, String> getHashMap78() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_ParameterUsed);
        return map;
    }

    // 下订单 CouponTabAdd
    public static HashMap<String, String> getHashMap77(String WeChatName, String WeChatPic,
                                                       String WeChatOpenid, String Title, String Amoney, String OrderID, String CouType) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_CouponTabAdd);
        map.put("WeChatName", WeChatName);
        map.put("WeChatPic", WeChatPic);
        map.put("WeChatOpenid", WeChatOpenid);
        map.put("Title", Title);
        map.put("Amoney", Amoney);
        map.put("OrderID", OrderID);
        map.put("CouType", CouType);
        return map;
    }

    //教练试学状态修改 TryTolearn_EditState
    public static HashMap<String, String> getHashMap76(String ID, int State) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_TryTolearn_EditState);
        map.put("ID", ID);
        map.put("State", State + "");

        return map;
    }

    //教练查看自己的试学 TryTolearn_ListDB
    public static HashMap<String, String> getHashMap75(String DistriCoach) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_TryTolearn_ListDB);
        map.put("DistriCoach", DistriCoach);

        return map;
    }

    //用户个人信息 UserInformation
    public static HashMap<String, String> getHashMap74(String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_UserInformation);
        map.put("Uid", Uid);
        return map;
    }


    // 教练认证附件查看 Coachfile_Sel
    public static HashMap<String, String> getHashMap73(String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_Coachfile_Sel);
        map.put("Uid", Uid);
        return map;
    }


    // 教练认证附件添加 Coachfile_Add
    public static HashMap<String, String> getHashMap72(String Uid, String PicSrc) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_Coachfile_Add);
        map.put("Uid", Uid);
        map.put("PicSrc", PicSrc);
        return map;
    }

    //我的视频收藏 Collection_Sel
    public static HashMap<String, String> getHashMap71(String Uid, int pageNum, int pageNumber) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_Collection_Sel);
        map.put("Uid", Uid);
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");
        return map;
    }

    // 试学 TryTolearn_Add
    public static HashMap<String, String> getHashMap70(String Name, String Phone, String
            Region, String Detailed, String DistriCoach) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_TryTolearn_Add);
        map.put("Name", Name);
        map.put("Phone", Phone);
        map.put("Region", Region);
        map.put("Detailed", Detailed);
        map.put("DistriCoach", DistriCoach);

        return map;
    }


    // 试驾 TestDrive_Add
    public static HashMap<String, String> getHashMap69(String Name, String Phone) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_TestDrive_Add);
        map.put("Name", Name);
        map.put("Phone", Phone);

        return map;
    }

    //我的陪练订单 SpOrdered_I
    public static HashMap<String, String> getHashMap68(String Uid) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_SpOrdered_I);
        map.put("Uid", Uid);
        return map;
    }

    //教练陪练订单 SpOrders_Coach
    public static HashMap<String, String> getHashMap67(String Uid) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_SpOrders_Coach);
        map.put("Uid", Uid);
        return map;
    }


    // 陪练信息添加 Sp_ordered_Add_nT
    public static HashMap<String, String> getHashMap66(String guid, String mg_address, String licenseH,
                                                       String Telephone, String Name, double longitude, double latitude, String Sp_proID,
                                                       String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_Sp_ordered_Add_nT);
        map.put("guid", guid);
        map.put("mg_address", mg_address);
        map.put("licenseH", licenseH);
        map.put("Telephone", Telephone);
        map.put("Name", Name);
        map.put("longitude", longitude + "");
        map.put("latitude", latitude + "");
        map.put("Sp_proID", Sp_proID);
        map.put("Uid", Uid);

        return map;
    }

    //陪练信息添加 Sp_ordered_Add
    public static HashMap<String, String> getHashMap65(String guid, String OrdAddress, String mg_address, String licenseH,
                                                       String Telephone, String Name, double longitude, double latitude, String Sp_proID, String LearnTime,
                                                       String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_Sp_ordered_Add);
        map.put("guid", guid);
        map.put("OrdAddress", OrdAddress);
        map.put("mg_address", mg_address);
        map.put("licenseH", licenseH);
        map.put("Telephone", Telephone);
        map.put("Name", Name);
        map.put("longitude", longitude + "");
        map.put("latitude", latitude + "");
        map.put("Sp_proID", Sp_proID);
        map.put("LearnTime", LearnTime);
        map.put("Uid", Uid);

        return map;
    }

    //陪练商品单价列表 UnitPriceList
    public static HashMap<String, String> getHashMap64(String PriceType) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_UnitPriceList);
        map.put("PriceType", PriceType);

        return map;
    }

    // 陪练商品详情 SparringSingle
    public static HashMap<String, String> getHashMap63(String Id) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_SparringSingle);
        map.put("Id", Id);
        return map;
    }

    // 陪练商品列表 SparringList
    public static HashMap<String, String> getHashMap62(int pageNum, int pageNumber) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_SparringList);
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");
        return map;
    }

    // 模糊查询视频 Video_Title
    public static HashMap<String, String> getHashMap61(String Vtitle, int pageNum, int pageNumber) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_Video_Title);
        map.put("Vtitle", Vtitle);
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");
        return map;
    }

    // 模糊查询教练 Coach_Title
    public static HashMap<String, String> getHashMap60(String Uname, int pageNum, int pageNumber) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_Coach_Title);
        map.put("Uname", Uname);
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");
        return map;
    }

    //获取语音列表
    public static HashMap<String, String> getHashMap59() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_Lighting);
        return map;
    }


    //获取语音列表
    public static HashMap<String, String> getHashMap58() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_ArticleFixedList);
        return map;
    }


    //获取区域信息
    public static HashMap<String, String> getHashMap57(String TotalID, String Comment,
                                                       String Head_portrait, String Name, String openid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_MapCommentAdd);
        map.put("TotalID", TotalID);
        map.put("Comment", Comment);
        map.put("Head_portrait", Head_portrait);
        map.put("Name", Name);
        map.put("openid", openid);
        return map;
    }


    //获取区域信息
    public static HashMap<String, String> getHashMap56() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_RegionList);
        return map;
    }

    //单教练信息 CoachSingle
    public static HashMap<String, String> getHashMap55(String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_CoachSingle);
        map.put("Uid", Uid);
        return map;
    }

    // 教练列表 CoachList
    public static HashMap<String, String> getHashMap54(String Type, String Region,
                                                       String Seniority, String Other, int pageNum, int pageNumber) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_CoachList);
        map.put("Type", Type);
        map.put("Region", Region);
        map.put("Seniority", Seniority);
        map.put("Other", Other);
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");
        return map;

    }


    //教练视频信息
    public static HashMap<String, String> getHashMap53(String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_CoachVideoList);
        map.put("Uid", Uid);
        return map;
    }

    //  openid查找个人信息 MapUser_I
    public static HashMap<String, String> getHashMap52(String openid) {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("nethod", method_MapUser_I);
        map.put("openid", openid);
        return map;
    }

    //微网站用户修改 Extension_User_Edit
    public static HashMap<String, String> getHashMap51(String ID, String openid,
                                                       String Name, String Head_portrait, String Telephone,
                                                       String CoachType, String Models, String Seniority, String Driver_school,
                                                       String Company, String Site_address, String CurrentAddress, double X, double Y,
                                                       String Province, String City, String Area, String Introduction, String PicSrc) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_Extension_User_Edit);
        map.put("ID", ID);
        map.put("openid", openid);
        map.put("Name", Name);
        map.put("Head_portrait", Head_portrait);
        map.put("Telephone", Telephone);
        map.put("CoachType", CoachType);
        map.put("Models", Models);
        map.put("Seniority", Seniority);
        map.put("Driver_school", Driver_school);
        map.put("Company", Company);
        map.put("Site_address", Site_address);
        map.put("CurrentAddress", CurrentAddress);
        map.put("X", X + "");
        map.put("Y", Y + "");
        map.put("Province", Province);
        map.put("City", City);
        map.put("Area", Area);
        map.put("Introduction", Introduction);
        map.put("PicSrc", PicSrc);
        return map;
    }

    // 微网站用户相册删除 MapUserPicDel
    public static HashMap<String, String> getHashMap50(String TotalID, String PicSrc) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_MapUserPicDel);
        map.put("TotalID", TotalID);
        map.put("PicSrc", PicSrc);
        return map;
    }

    //补充用户资料 UserSystemEidt
    public static HashMap<String, String> getHashMap49(String Uid, String Pwd, int Type) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_UserSystemEidt);
        map.put("Uid", Uid);
        map.put("Pwd", Pwd);
        map.put("Type", Type + "");
        return map;
    }

    //绑定微信(未注册用户系统自动注册)
    public static HashMap<String, String> getHashMap48(String Uaccount, String opendid,
                                                       String wx_nickname, String wx_portrait,
                                                       String wx_unionid, String identifyCode) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_UserWeChat_UaccountAdd);
        map.put("Uaccount", Uaccount);
        map.put("opendid", opendid);
        map.put("wx_nickname", wx_nickname);
        map.put("wx_portrait", wx_portrait);
        map.put("wx_unionid", wx_unionid);
        map.put("identifyCode", identifyCode);
        return map;
    }

    // 微信登录
    public static HashMap<String, String> getHashMap47(String openid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_UserLogin_WeChat);
        map.put("openid", openid);
        return map;
    }


    // 判断用户登录或绑定微信 WeChatJudgment
    public static HashMap<String, String> getHashMap46(String openid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_WeChatJudgment);
        map.put("openid", openid);
        return map;
    }

    // 微网站用户相册上传 MapUserPicAdd
    public static HashMap<String, String> getHashMap45(String TotalID, String PicSrc) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_MapUserPicAdd);
        map.put("TotalID", TotalID);
        map.put("PicSrc", PicSrc);
        return map;
    }

    //微网站用户增加 Extension_User_Add
    public static HashMap<String, String> getHashMap44(String ID, String openid,
                                                       String Name, String Head_portrait, String Telephone,
                                                       String CoachType, String Models, String Seniority, String Driver_school,
                                                       String Company, String Site_address, String CurrentAddress, double X, double Y,
                                                       String Province, String City, String Area, String Introduction) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_Extension_User_Add);
        map.put("ID", ID);
        map.put("openid", openid);
        map.put("Name", Name);
        map.put("Head_portrait", Head_portrait);
        map.put("Telephone", Telephone);
        map.put("CoachType", CoachType);
        map.put("Models", Models);
        map.put("Seniority", Seniority);
        map.put("Driver_school", Driver_school);
        map.put("Company", Company);
        map.put("Site_address", Site_address);
        map.put("CurrentAddress", CurrentAddress);
        map.put("X", X + "");
        map.put("Y", Y + "");
        map.put("Province", Province);
        map.put("City", City);
        map.put("Area", Area);
        map.put("Introduction", Introduction);
        return map;
    }

    //图片上传参数
    public static HashMap<String, String> getHashMap43(int Type, String Data) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Type", Type + "");
        map.put("Data", Data);
        return map;
    }

    // 绑定微信
    public static HashMap<String, String> getHashMap42(String Uid, String opendid,
                                                       String wx_nickname, String wx_portrait,
                                                       String wx_unionid) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_UserWeChatAdd);
        map.put("Uid", Uid);
        map.put("opendid", opendid);
        map.put("wx_nickname", wx_nickname);
        map.put("wx_portrait", wx_portrait);
        map.put("wx_unionid", wx_unionid);
        return map;
    }

    //微网站评论列表 MapCommentList
    public static HashMap<String, String> getHashMap41(String Uid, int pageNum, int pageNumber) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_MapCommentList);
        map.put("Uid", Uid);
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");
        return map;
    }

    public static HashMap<String, String> getHashMap40() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_User_school);
        return map;
    }

    // 微网站个人相册 MapUserPic
    public static HashMap<String, String> getHashMap39(String Uid, int pageNum, int pageNumber) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_MapUserPic);
        map.put("Uid", Uid);
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");
        return map;
    }

    //  微网站个人信息 MapUser
    public static HashMap<String, String> getHashMap38(String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_MapUser);
        map.put("Uid", Uid);

        return map;
    }

    // 当前用户周围3公里用户 MapListUser(有距离数据)
    public static HashMap<String, String> getHashMap37(String CType, String JL_Type, String Seniority,
                                                       String Driver_school, double longitude, double latitude) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_MapListUser);
        map.put("CType", CType);
        map.put("JL_Type", JL_Type + "");
        map.put("Seniority", Seniority + "");
        map.put("Driver_school", Driver_school);
        map.put("longitude", longitude + "");
        map.put("latitude", latitude + "");
        return map;
    }


    // 当前用户周围3公里用户 MapList
    public static HashMap<String, String> getHashMap36(double longitude, double latitude) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_MapList);
        map.put("longitude", longitude + "");
        map.put("latitude", latitude + "");
        return map;
    }

    // 视频列表(分类ID) VideoListType
    public static HashMap<String, String> getHashMap35(String TypeID, int pageNum, int pageNumber) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_VideoListType);
        map.put("TypeID", TypeID);
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");
        return map;
    }

    // 获取视频分类
    public static HashMap<String, String> getHashMap34(int Type, int Category) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_VideoType);
        map.put("Type", Type + "");
        map.put("Category", Category + "");
        return map;
    }

    // 添加评论
    public static HashMap<String, String> getHashMap33(String Vid, String Uid, String Content) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_CommentAdd);
        map.put("Vid", Vid);
        map.put("Uid", Uid);
        map.put("Content", Content);

        return map;

    }

    // 获取评论列表
    public static HashMap<String, String> getHashMap32(String Vid, int pageNum, int pageNumber) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", methmethod_CommentList);
        map.put("Vid", Vid);
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");
        return map;
    }


    // 视频点击量
    public static HashMap<String, String> getHashMap31(String Vid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_Vide_Edit_Playnumber);
        map.put("Vid", Vid);
        return map;
    }


    // 取消收藏
    public static HashMap<String, String> getHashMap30(String Cid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_CollectionDelete);
        map.put("Cid", Cid);
        return map;
    }

    // 收藏
    public static HashMap<String, String> getHashMap29(String Vid, String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_CollectionAdd);
        map.put("Vid", Vid);
        map.put("Uid", Uid);
        return map;

    }

    // 取消点赞
    public static HashMap<String, String> getHashMap28(String Zid, String Vid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_ThingDelete);
        map.put("Zid", Zid);
        map.put("Vid", Vid);
        return map;
    }

    // 点赞
    public static HashMap<String, String> getHashMap27(String Vid, String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_ThingAdd);
        map.put("Vid", Vid);
        map.put("Uid", Uid);

        return map;
    }

    // 视频详情
    public static HashMap<String, String> getHashMap26(String Vid, String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_VideoDetails);
        map.put("Vid", Vid);
        map.put("Uid", Uid);

        return map;
    }

    // 文章详情 NoviceListID
    public static HashMap<String, String> getHashMap25(String Nid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_NoviceListID);
        map.put("Nid", Nid);
        return map;
    }

    // 文章列表
    public static HashMap<String, String> getHashMap24(int Type, int pageNum, int pageNumber) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_NoviceList);
        map.put("Type", Type + "");
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");

        return map;

    }

    // 视频列表 VideoList
    public static HashMap<String, String> getHashMap23(int Type, int pageNum, int pageNumber) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_VideoList);
        map.put("Type", Type + "");
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");

        return map;
    }

    // 排行榜 method_MnksRankingPX
    public static HashMap<String, String> getHashMap22(String Uid, int Type) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_MnksRankingPX);
        map.put("Uid", Uid);
        map.put("Type", Type + "");
        return map;
    }

    // 考试记录 MnksRankingJL
    public static HashMap<String, String> getHashMap21(String Uid, int Type) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_MnksRankingJL);
        map.put("Uid", Uid);
        map.put("Type", Type + "");
        return map;
    }

    // 排行、科目添加
    public static HashMap<String, String> getHashMap20(String ExamTime, int Rantype, int Fraction, int Type, String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_MnksRanking);
        map.put("ExamTime", ExamTime);
        map.put("Rantype", Rantype + "");
        map.put("Fraction", Fraction + "");
        map.put("Type", Type + "");
        map.put("Uid", Uid);
        return map;
    }


    //提交答题记录
    public static HashMap<String, String> getHashMap19(int Type, String Uid, String T_TitleID, String T_option) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_TitleRecord_Edit);
        map.put("Type", Type + "");
        map.put("Uid", Uid);
        map.put("T_TitleID", T_TitleID);
        map.put("T_option", T_option);

        return map;
    }

    // 模拟考试 TitleRecord_MNKS
    public static HashMap<String, String> getHashMap18(int Type, String Uid, int IsPostBack, int pageNum, int pageNumber) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_TitleRecord_MNKS);
        map.put("Type", Type + "");
        map.put("Uid", Uid);
        map.put("IsPostBack", IsPostBack + "");
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");
        return map;
    }

    // 查看收藏 ColnTopicSel
    public static HashMap<String, String> getHashMap17(String Uid, int Type, int pageNum, int pageNumber) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_ColnTopicSel);
        map.put("Uid", Uid);
        map.put("Type", Type + "");
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");
        return map;

    }

    //错题列表
    public static HashMap<String, String> getHashMap16(int Type, String Uid, int pageNum, int pageNumber) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_TitleError_List);
        map.put("Type", Type + "");
        map.put("Uid", Uid);
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");
        return map;
    }

    // 消除错题
    public static HashMap<String, String> getHashMap15(String Uid, String T_TitleID) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_TitleError_Del);
        map.put("Uid", Uid);
        map.put("T_TitleID", T_TitleID);
        return map;
    }

    //添加错题
    public static HashMap<String, String> getHashMap14(int Type, String Uid, String T_TitleID) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_TitleError_Add);
        map.put("Type", Type + "");
        map.put("Uid", Uid);
        map.put("T_TitleID", T_TitleID);
        return map;
    }

    //题目记录
    public static HashMap<String, String> getHashMap13(String ChapterID, int RecType, int Number, int Type, String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_Record);
        map.put("ChapterID", ChapterID + "");
        map.put("RecType", RecType + "");
        map.put("Number", Number + "");
        map.put("Type", Type + "");
        map.put("Uid", Uid);

        return map;
    }

    //首页数量
    public static HashMap<String, String> getHashMap12(int Type, String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_OneSubjectCount);
        map.put("Type", Type + "");
        map.put("Uid", Uid);
        return map;
    }

    //主页图集
    public static HashMap<String, String> getHashMap11(int Type) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_CarouselList);
        map.put("Type", Type + "");
        return map;

    }

    //取消收藏 ColnTopicDel
    public static HashMap<String, String> getHashMap10(String ID, String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_ColnTopicDel);
        map.put("ID", ID);
        map.put("Uid", Uid);
        return map;
    }

    //题目收藏 ColnTopicAdd
    public static HashMap<String, String> getHashMap9(String T_TitleID, String Uid, int T_Subject) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_ColnTopicAdd);
        map.put("T_TitleID", T_TitleID);
        map.put("Uid", Uid);
        map.put("T_Subject", T_Subject + "");
        return map;
    }

    //  忘记密码 UserForgetPwd
    public static HashMap<String, String> getHashMap8(String Uaccount, String NewPwd, int Type, String identifyCode) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_UserForgetPwd);
        map.put("Uaccount", Uaccount);
        map.put("NewPwd", NewPwd);
        map.put("Type", Type + "");
        map.put("identifyCode", identifyCode);
        return map;

    }


    // 验证码登录 UserInform
    public static HashMap<String, String> getHashMap7(String Uaccount, String Type, String identifyCode) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_UserInform);
        map.put("Uaccount", Uaccount);
        map.put("Type", Type);
        map.put("identifyCode", identifyCode);
        return map;

    }

    // 用户注册 UserRegs
    public static HashMap<String, String> getHashMap6(String Uaccount, String UPwd, int Utype, String yqm, String identifyCode) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_UserRegs);
        map.put("Uaccount", Uaccount);
        map.put("UPwd", UPwd);
        map.put("Utype", Utype + "");
        map.put("yqm", yqm);
        map.put("identifyCode", identifyCode);
        return map;
    }

    // 答案 TitleAnswerList
    public static HashMap<String, String> getHashMap5(String A_TitleID) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_TitleAnswerList);
        map.put("A_TitleID", A_TitleID);
        return map;
    }

    //账号密码登录
    public static HashMap<String, String> getHashMap4(String Uaccount, String Upwd, String Type) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_UserLogin);
        map.put("Uaccount", Uaccount);
        map.put("Upwd", Upwd);
        map.put("Type", Type);
        return map;
    }

    //题目加答案
    public static HashMap<String, String> getHashMap3(int Type, String T_Subject, int pageNum, int pageNumber, String Uid) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_TitleList_KM);
        map.put("Type", Type + "");
        map.put("T_Subject", T_Subject);
        map.put("pageNum", pageNum + "");
        map.put("pageNumber", pageNumber + "");
        map.put("Uid", Uid);
        return map;
    }

    //理论章节
    public static HashMap<String, String> getHashMap2(int Type, String Uid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_ChapterList);
        map.put("Type", Type + "");
        map.put("Uid", Uid);
        return map;
    }


    // 短信请求
    public static HashMap<String, String> getHashMap1(String Phone, int SendType) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("method", method_SendSMS);
        map.put("Phone", Phone);
        map.put("SendType", SendType + "");
        return map;
    }


    /**
     * 短信请求
     */
    public static void sendSendSMS(final Handler handler, String phone, int SendType
    ) {

        String sign = null;

        try {

            sign = getSignature(getHashMap1(phone, SendType), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }

        HttpParams param = new HttpParams();

        param.put("method", method_SendSMS);
        param.put("Phone", phone);
        param.put("SendType", SendType);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    // 获取理论章节
    public static void getChapterList(final Handler handler, int Type, String Uid) {
        String sign = null;

        try {

            sign = getSignature(getHashMap2(Type, Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }

        HttpParams param = new HttpParams();

        param.put("method", method_ChapterList);
        param.put("Type", Type);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 题目+正确答案 TitleList_KM
     *
     * @param handler
     * @param Type
     * @param T_Subject
     * @param pageNum
     * @param pageNumber
     */
    public static void getTitleList_KM(final Handler handler, int Type, String T_Subject, int pageNum, int pageNumber, String Uid) {
        String sign = null;

        try {

            sign = getSignature(getHashMap3(Type, T_Subject, pageNum, pageNumber, Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }

        HttpParams param = new HttpParams();
        param.put("method", method_TitleList_KM);
        param.put("Type", Type);
        param.put("T_Subject", T_Subject);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    public static void getTitleAnswerList(final Handler handler, String A_TitleID) {
        String sign = null;

        try {

            sign = getSignature(getHashMap5(A_TitleID), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }

        HttpParams param = new HttpParams();
        param.put("method", method_TitleAnswerList);
        param.put("A_TitleID", A_TitleID);
        param.put("sign", sign);
        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }


    // 账号密码登录
    public static void login_UserLogin(final Handler handler, String Uaccount, String Upwd, String Type) {
        String sign = null;

        try {

            sign = getSignature(getHashMap4(Uaccount, Upwd, Type), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }

        HttpParams param = new HttpParams();
        param.put("method", method_UserLogin);
        param.put("Uaccount", Uaccount);
        param.put("Upwd", Upwd);
        param.put("Type", Type);
        param.put("sign", sign);
        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 注册界面
     *
     * @param handler
     * @param Uaccount
     * @param UPwd
     * @param Utype
     * @param yqm
     * @param identifyCode
     */
    public static void registerUserRegs(final Handler handler, String Uaccount, String UPwd, int Utype, String yqm, String identifyCode) {
        String sign = null;

        try {
            sign = getSignature(getHashMap6(Uaccount, UPwd, Utype, yqm, identifyCode), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }

        HttpParams param = new HttpParams();
        param.put("method", method_UserRegs);
        param.put("Uaccount", Uaccount);
        param.put("UPwd", UPwd);
        param.put("Utype", Utype);
        param.put("yqm", yqm);
        param.put("identifyCode", identifyCode);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 验证码登录 UserInform
     *
     * @param handler
     * @param Uaccount
     * @param Type
     * @param identifyCode
     */
    public static void login_UserInform(final Handler handler, String Uaccount, String Type, String identifyCode) {
        String sign = null;

        try {
            sign = getSignature(getHashMap7(Uaccount, Type, identifyCode), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();

        param.put("method", method_UserInform);
        param.put("Uaccount", Uaccount);
        param.put("Type", Type);
        param.put("identifyCode", identifyCode);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 忘记密码 UserForgetPwd
     *
     * @param handler
     * @param Uaccount
     * @param NewPwd
     * @param Type
     * @param identifyCode
     */
    public static void fandUserForgetPwd(final Handler handler, String Uaccount, String NewPwd, int Type, String identifyCode) {

        String sign = null;

        try {
            sign = getSignature(getHashMap8(Uaccount, NewPwd, Type, identifyCode), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_UserForgetPwd);
        param.put("Uaccount", Uaccount);
        param.put("NewPwd", NewPwd);
        param.put("Type", Type);
        param.put("identifyCode", identifyCode);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 题目收藏 ColnTopicAdd
     *
     * @param handler
     * @param T_TitleID
     * @param Uid
     * @param T_Subject
     */
    public static void colnColnTopicAdd(final Handler handler, String T_TitleID, String Uid, int T_Subject) {
        String sign = null;

        try {
            sign = getSignature(getHashMap9(T_TitleID, Uid, T_Subject), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_ColnTopicAdd);
        param.put("T_TitleID", T_TitleID);
        param.put("Uid", Uid);
        param.put("T_Subject", T_Subject);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 取消收藏 ColnTopicDel
     *
     * @param handler
     * @param ID
     * @param Uid
     */
    public static void delColnTopicDel(final Handler handler, String ID, String Uid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap10(ID, Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_ColnTopicDel);
        param.put("ID", ID);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    //主页图集
    public static void getCarouselList(final Handler handler, int Type) {

        String sign = null;

        try {
            sign = getSignature(getHashMap11(Type), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_CarouselList);
        param.put("Type", Type);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    // 错题数量等
    public static void getOneSubjectCount(final Handler handler, int Type, String Uid) {
        String sign = null;

        try {
            sign = getSignature(getHashMap12(Type, Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_OneSubjectCount);
        param.put("Type", Type);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }


    //题目记录 Record
    public static void setRecord(final Handler handler, String ChapterID, int RecType, int Number, int Type, String Uid) {
        String sign = null;

        try {
            sign = getSignature(getHashMap13(ChapterID, RecType, Number, Type, Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_Record);
        param.put("ChapterID", ChapterID);
        param.put("RecType", RecType);
        param.put("Number", Number);
        param.put("Type", Type);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 添加错题 TitleError_Add
     *
     * @param handler
     * @param Type
     * @param Uid
     * @param T_TitleID
     */
    public static void addTitleError_Add(final Handler handler, int Type, String Uid, String T_TitleID) {
        String sign = null;

        try {
            sign = getSignature(getHashMap14(Type, Uid, T_TitleID), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_TitleError_Add);
        param.put("Type", Type);
        param.put("Uid", Uid);
        param.put("T_TitleID", T_TitleID);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 消除错题 TitleError_Del
     *
     * @param handler
     * @param Uid
     * @param T_TitleID
     */
    public static void delTitleError_Del(final Handler handler, String Uid, String T_TitleID) {
        String sign = null;

        try {
            sign = getSignature(getHashMap15(Uid, T_TitleID), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_TitleError_Del);
        param.put("Uid", Uid);
        param.put("T_TitleID", T_TitleID);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 错题列表 TitleError_List
     *
     * @param handler
     * @param Uid
     * @param pageNum
     * @param pageNumber
     */
    public static void getTitleError_List(final Handler handler, int Type, String Uid, int pageNum, int pageNumber) {
        String sign = null;

        try {
            sign = getSignature(getHashMap16(Type, Uid, pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_TitleError_List);
        param.put("Type", Type);
        param.put("Uid", Uid);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 查看收藏 ColnTopicSel
     *
     * @param handler
     * @param Uid
     * @param Type
     * @param pageNum
     * @param pageNumber
     */
    public static void getColnTopicSel(final Handler handler, String Uid, int Type, int pageNum, int pageNumber) {
        String sign = null;

        try {
            sign = getSignature(getHashMap17(Uid, Type, pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_ColnTopicSel);
        param.put("Uid", Uid);
        param.put("Type", Type);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 模拟考试 TitleRecord_MNKS
     *
     * @param handler
     * @param Type
     * @param Uid
     * @param IsPostBack
     * @param pageNum
     * @param pageNumber
     */
    public static void getTitleRecord_MNKS(final Handler handler, int Type, String Uid, int IsPostBack, int pageNum, int pageNumber) {
        String sign = null;

        try {
            sign = getSignature(getHashMap18(Type, Uid, IsPostBack, pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_TitleRecord_MNKS);
        param.put("Type", Type);
        param.put("Uid", Uid);
        param.put("IsPostBack", IsPostBack);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    public static void upTitleRecord_Edit(final Handler handler, int Type, String Uid, String T_TitleID, String T_option) {
        String sign = null;

        try {
            sign = getSignature(getHashMap19(Type, Uid, T_TitleID, T_option), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_TitleRecord_Edit);
        param.put("Type", Type);
        param.put("Uid", Uid);
        param.put("T_TitleID", T_TitleID);
        param.put("T_option", T_option);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 排行、科目添加 MnksRanking
     *
     * @param handler
     * @param ExamTime
     * @param Rantype
     * @param Fraction
     * @param Type
     * @param Uid
     */
    public static void addMnksRanking(final Handler handler, String ExamTime, int Rantype, int Fraction, int Type, String Uid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap20(ExamTime, Rantype, Fraction, Type, Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_MnksRanking);
        param.put("ExamTime", ExamTime);
        param.put("Rantype", Rantype);
        param.put("Fraction", Fraction);
        param.put("Type", Type);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 获取考试记录
     *
     * @param handler
     * @param Uid
     * @param Type
     */
    public static void getMnksRankingJL(final Handler handler, String Uid, int Type) {
        String sign = null;

        try {
            sign = getSignature(getHashMap21(Uid, Type), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_MnksRankingJL);
        param.put("Uid", Uid);
        param.put("Type", Type);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }


    /**
     * 获取排行榜
     *
     * @param handler
     * @param Uid
     * @param Type
     */
    public static void getMnksRankingPX(final Handler handler, String Uid, int Type) {
        String sign = null;

        try {
            sign = getSignature(getHashMap22(Uid, Type), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_MnksRankingPX);
        param.put("Uid", Uid);
        param.put("Type", Type);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }


    /**
     * 视频列表 VideoList
     *
     * @param handler
     * @param Type
     * @param pageNum
     * @param pageNumber
     */
    public static void getVideoList(final Handler handler, int Type, int pageNum, int pageNumber) {
        String sign = null;

        try {
            sign = getSignature(getHashMap23(Type, pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_VideoList);
        param.put("Type", Type);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 文章列表 NoviceList
     *
     * @param handler
     * @param Type
     * @param pageNum
     * @param pageNumber
     */
    public static void getNoviceList(final Handler handler, int Type, int pageNum, int pageNumber) {

        String sign = null;

        try {
            sign = getSignature(getHashMap24(Type, pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();

        param.put("method", method_NoviceList);
        param.put("Type", Type);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 文章详情 NoviceListID
     *
     * @param handler
     * @param Nid
     */
    public static void getNoviceListID(final Handler handler, String Nid) {
        String sign = null;

        try {
            sign = getSignature(getHashMap25(Nid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_NoviceListID);
        param.put("Nid", Nid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取视频详情
     *
     * @param handler
     * @param Vid
     * @param Uid
     */
    public static void getVideoDetails(final Handler handler, String Vid, String Uid) {
        String sign = null;

        try {
            sign = getSignature(getHashMap26(Vid, Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_VideoDetails);
        param.put("Vid", Vid);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 点赞
     *
     * @param handler
     * @param Vid
     * @param Uid
     */
    public static void zanThingAdd(final Handler handler, String Vid, String Uid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap27(Vid, Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_ThingAdd);
        param.put("Vid", Vid);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 取消点赞
     *
     * @param handler
     * @param Zid
     */
    public static void deleteThingDelete(final Handler handler, String Zid, String Vid) {
        String sign = null;

        try {
            sign = getSignature(getHashMap28(Zid, Vid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_ThingDelete);
        param.put("Zid", Zid);
        param.put("Vid", Vid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 收藏
     *
     * @param handler
     * @param Vid
     * @param Uid
     */
    public static void collectCollectionAdd(final Handler handler, String Vid, String Uid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap29(Vid, Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_CollectionAdd);
        param.put("Vid", Vid);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 取消收藏
     *
     * @param handler
     * @param Cid
     */
    public static void deleteCollectionDelete(final Handler handler, String Cid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap30(Cid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_CollectionDelete);
        param.put("Cid", Cid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 视频点击量 Vide_Edit_Playnumber
     *
     * @param handler
     * @param Vid
     */
    public static void setVide_Edit_Playnumber(final Handler handler, String Vid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap31(Vid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_Vide_Edit_Playnumber);
        param.put("Vid", Vid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }


    /**
     * 评论列表 CommentList
     *
     * @param handler
     * @param Vid
     */
    public static void getCommentList(final Handler handler, String Vid, int pageNum, int pageNumber) {
        String sign = null;

        try {
            sign = getSignature(getHashMap32(Vid, pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }

        HttpParams param = new HttpParams();
        param.put("method", methmethod_CommentList);
        param.put("Vid", Vid);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }


    /**
     * 添加评论
     *
     * @param handler
     * @param Vid
     * @param Uid
     * @param Content
     */
    public static void addCommentAdd(final Handler handler, String Vid, String Uid, String Content) {

        String sign = null;

        try {
            sign = getSignature(getHashMap33(Vid, Uid, Content), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_CommentAdd);
        param.put("Vid", Vid);
        param.put("Uid", Uid);
        param.put("Content", Content);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取视频分类列表
     *
     * @param handler
     * @param Type
     * @param Category
     */
    public static void getVideoType(final Handler handler, int Type, int Category) {

        String sign = null;

        try {
            sign = getSignature(getHashMap34(Type, Category), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_VideoType);
        param.put("Type", Type);
        param.put("Category", Category);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 视频列表(分类ID) VideoListType
     *
     * @param handler
     * @param TypeID
     * @param pageNum
     * @param pageNumber
     */
    public static void getVideoListType(final Handler handler, String TypeID, int pageNum, int pageNumber) {
        String sign = null;

        try {
            sign = getSignature(getHashMap35(TypeID, pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_VideoListType);
        param.put("TypeID", TypeID);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取当前用户周围3公里用户
     *
     * @param handler
     * @param longitude
     * @param latitude
     */
    public static void getMapList(final Handler handler, double longitude, double latitude) {
        String sign = null;

        try {
            sign = getSignature(getHashMap36(longitude, latitude), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_MapList);
        param.put("longitude", longitude);
        param.put("latitude", latitude);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 当前用户周围3公里用户 MapListUser(有距离数据)
     *
     * @param handler
     * @param CType
     * @param JL_Type
     * @param Seniority
     * @param Driver_school
     * @param longitude
     * @param latitude
     */
    public static void getMapListUser(final Handler handler, String CType, String JL_Type, String Seniority,
                                      String Driver_school, double longitude, double latitude) {

        String sign = null;

        try {
            sign = getSignature(getHashMap37(CType, JL_Type, Seniority, Driver_school, longitude, latitude), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_MapListUser);
        param.put("CType", CType);
        param.put("JL_Type", JL_Type);
        param.put("Seniority", Seniority);
        param.put("Driver_school", Driver_school);
        param.put("longitude", longitude);
        param.put("latitude", latitude);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 微网站个人信息 MapUser
     *
     * @param handler
     * @param Uid
     */
    public static void getMapUser(final Handler handler, String Uid) {
        String sign = null;

        try {
            sign = getSignature(getHashMap38(Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_MapUser);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 微网站个人相册 MapUserPic
     *
     * @param handler
     * @param Uid
     */
    public static void getMapUserPic(final Handler handler, String Uid, int pageNum, int pageNumber) {

        String sign = null;

        try {
            sign = getSignature(getHashMap39(Uid, pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_MapUserPic);
        param.put("Uid", Uid);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取驾校列表
     *
     * @param handler
     */
    public static void getUser_School(final Handler handler) {

        String sign = null;

        try {
            sign = getSignature(getHashMap40(), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_User_school);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }


    public static void getMapCommentList(final Handler handler, String Uid, int pageNum, int pageNumber) {
        String sign = null;

        try {
            sign = getSignature(getHashMap41(Uid, pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_MapCommentList);
        param.put("Uid", Uid);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 绑定微信 UserWeChatAdd
     *
     * @param handler
     * @param Uid
     * @param opendid
     * @param wx_nickname
     * @param wx_portrait
     * @param wx_unionid
     */
    public static void addUserWeChatAdd(final Handler handler, String Uid, String opendid,
                                        String wx_nickname, String wx_portrait,
                                        String wx_unionid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap42(Uid, opendid,
                    wx_nickname, wx_portrait, wx_unionid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_UserWeChatAdd);
        param.put("Uid", Uid);
        param.put("opendid", opendid);
        param.put("wx_nickname", wx_nickname);
        param.put("wx_portrait", wx_portrait);
        param.put("wx_unionid", wx_unionid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 传图片
     *
     * @param filePath
     * @return
     */
    public static RequestParams loadPic(int Type, String filePath) {

        File tempFile = new File(filePath);

        String sign = null;

        try {
            sign = getSignature(getHashMap43(Type, tempFile + ""), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }


        RequestParams params = new RequestParams();
        params.addBodyParameter("Type", Type + "");
        params.addBodyParameter("Data", tempFile.toString());
        params.addBodyParameter(tempFile.getPath().replace("/", ""), tempFile);
        params.addBodyParameter("sign", sign);

        return params;
    }

    /**
     * 微网站用户增加 Extension_User_Add
     *
     * @param handler
     * @param ID
     * @param openid
     * @param Name
     * @param Head_portrait
     * @param Telephone
     * @param CoachType
     * @param Models
     * @param Seniority
     * @param Driver_school
     * @param Company
     * @param Site_address
     * @param CurrentAddress
     * @param X
     * @param Y
     * @param Province
     * @param City
     * @param Area
     * @param Introduction
     */
    public static void addExtension_User_Add(final Handler handler, String ID, String openid,
                                             String Name, String Head_portrait, String Telephone,
                                             String CoachType, String Models, String Seniority, String Driver_school,
                                             String Company, String Site_address, String CurrentAddress, double X, double Y,
                                             String Province, String City, String Area, String Introduction) {

        String sign = null;

        try {
            sign = getSignature(getHashMap44(ID, openid, Name, Head_portrait, Telephone, CoachType, Models,
                    Seniority, Driver_school, Company, Site_address, CurrentAddress, X, Y, Province, City, Area,
                    Introduction), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_Extension_User_Add);
        param.put("ID", ID);
        param.put("openid", openid);
        param.put("Name", Name);
        param.put("Head_portrait", Head_portrait);
        param.put("Telephone", Telephone);
        param.put("CoachType", CoachType);
        param.put("Models", Models);
        param.put("Seniority", Seniority);
        param.put("Driver_school", Driver_school);
        param.put("Company", Company);
        param.put("Site_address", Site_address);
        param.put("CurrentAddress", CurrentAddress);
        param.put("X", X);
        param.put("Y", Y);
        param.put("Province", Province);
        param.put("City", City);
        param.put("Area", Area);
        param.put("Introduction", Introduction);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 微网站用户相册上传 MapUserPicAdd
     *
     * @param handler
     * @param TotalID
     * @param PicSrc
     */
    public static void addMapUserPicAdd(final Handler handler, String TotalID, String PicSrc) {

        String sign = null;

        try {
            sign = getSignature(getHashMap45(TotalID, PicSrc), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_MapUserPicAdd);
        param.put("TotalID", TotalID);
        param.put("PicSrc", PicSrc);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    // 判断用户登录或绑定微信 WeChatJudgment
    public static void judgeWeChatJudgment(final Handler handler, String openid) {
        String sign = null;

        try {
            sign = getSignature(getHashMap46(openid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_WeChatJudgment);
        param.put("openid", openid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    //微信登录 UserLogin_WeChat
    public static void loginUserLogin_WeChat(final Handler handler, String openid) {
        String sign = null;

        try {
            sign = getSignature(getHashMap47(openid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_UserLogin_WeChat);
        param.put("openid", openid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 绑定微信(未注册用户系统自动注册)
     *
     * @param handler
     * @param Uaccount
     * @param opendid
     * @param wx_nickname
     * @param wx_portrait
     * @param wx_unionid
     * @param identifyCode
     */
    public static void addUserWeChat_UaccountAdd(final Handler handler, String Uaccount, String opendid,
                                                 String wx_nickname, String wx_portrait,
                                                 String wx_unionid, String identifyCode) {
        String sign = null;

        try {
            sign = getSignature(getHashMap48(Uaccount, opendid, wx_nickname, wx_portrait, wx_unionid, identifyCode), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_UserWeChat_UaccountAdd);
        param.put("Uaccount", Uaccount);
        param.put("opendid", opendid);
        param.put("wx_nickname", wx_nickname);
        param.put("wx_portrait", wx_portrait);
        param.put("wx_unionid", wx_unionid);
        param.put("identifyCode", identifyCode);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 补充用户资料
     *
     * @param handler
     * @param Uid
     * @param Pwd
     * @param Type
     */
    public static void addUserSystemEidt(final Handler handler, String Uid, String Pwd, int Type) {

        String sign = null;

        try {
            sign = getSignature(getHashMap49(Uid, Pwd, Type), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_UserSystemEidt);
        param.put("Uid", Uid);
        param.put("Pwd", Pwd);
        param.put("Type", Type);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 微网站用户相册删除 MapUserPicDel
     *
     * @param handler
     * @param TotalID
     * @param PicSrc
     */
    public static void deleteMapUserPicDel(final Handler handler, String TotalID, String PicSrc) {

        String sign = null;

        try {
            sign = getSignature(getHashMap50(TotalID, PicSrc), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_MapUserPicDel);
        param.put("TotalID", TotalID);
        param.put("PicSrc", PicSrc);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }


    /**
     * 微网站用户修改 Extension_User_Edit
     *
     * @param handler
     * @param ID
     * @param openid
     * @param Name
     * @param Head_portrait
     * @param Telephone
     * @param CoachType
     * @param Models
     * @param Seniority
     * @param Driver_school
     * @param Company
     * @param Site_address
     * @param CurrentAddress
     * @param X
     * @param Y
     * @param Province
     * @param City
     * @param Area
     * @param Introduction
     */
    public static void editExtension_User_Edit(final Handler handler, String ID, String openid,
                                               String Name, String Head_portrait, String Telephone,
                                               String CoachType, String Models, String Seniority, String Driver_school,
                                               String Company, String Site_address, String CurrentAddress, double X, double Y,
                                               String Province, String City, String Area, String Introduction, String PicSrc) {

        String sign = null;

        try {
            sign = getSignature(getHashMap51(ID, openid, Name, Head_portrait, Telephone, CoachType, Models,
                    Seniority, Driver_school, Company, Site_address, CurrentAddress, X, Y, Province, City, Area,
                    Introduction, PicSrc), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_Extension_User_Edit);
        param.put("ID", ID);
        param.put("openid", openid);
        param.put("Name", Name);
        param.put("Head_portrait", Head_portrait);
        param.put("Telephone", Telephone);
        param.put("CoachType", CoachType);
        param.put("Models", Models);
        param.put("Seniority", Seniority);
        param.put("Driver_school", Driver_school);
        param.put("Company", Company);
        param.put("Site_address", Site_address);
        param.put("CurrentAddress", CurrentAddress);
        param.put("X", X);
        param.put("Y", Y);
        param.put("Province", Province);
        param.put("City", City);
        param.put("Area", Area);
        param.put("Introduction", Introduction);
        param.put("PicSrc", PicSrc);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * openid查找个人信息 MapUser_I
     *
     * @param handler
     * @param openid
     */
    public static void getMapUser_I(final Handler handler, String openid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap52(openid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("nethod", method_MapUser_I);
        param.put("openid", openid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 获取教练信息
     *
     * @param handler
     * @param Uid
     */
    public static void getCoachVideoList(final Handler handler, String Uid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap53(Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_CoachVideoList);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 教练列表 CoachList
     *
     * @param handler
     * @param Type
     * @param Region
     * @param Seniority
     * @param Other
     * @param pageNum
     * @param pageNumber
     */
    public static void getCoachList(final Handler handler, String Type, String Region,
                                    String Seniority, String Other, int pageNum, int pageNumber) {

        String sign = null;

        try {
            sign = getSignature(getHashMap54(Type, Region, Seniority, Other, pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_CoachList);
        param.put("Type", Type);
        param.put("Region", Region);
        param.put("Seniority", Seniority);
        param.put("Other", Other);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });


    }

    /**
     * 获取单教练信息
     *
     * @param handler
     * @param Uid
     */
    public static void getCoachSingle(final Handler handler, String Uid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap55(Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_CoachSingle);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 获取地区
     *
     * @param handler
     */
    public static void getAreaRegionList(final Handler handler) {

        String sign = null;

        try {
            sign = getSignature(getHashMap56(), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_RegionList);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 微网站用户评论 MapCommentAdd
     *
     * @param handler
     * @param TotalID
     * @param Comment
     * @param Head_portrait
     * @param Name
     * @param openid
     */
    public static void addMapCommentAdd(final Handler handler, String TotalID, String Comment,
                                        String Head_portrait, String Name, String openid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap57(TotalID, Comment, Head_portrait, Name, openid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_MapCommentAdd);
        param.put("TotalID", TotalID);
        param.put("Comment", Comment);
        param.put("Head_portrait", Head_portrait);
        param.put("Name", Name);
        param.put("openid", openid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 获取特定灯光列表
     *
     * @param handler
     */
    public static void getLightingList(final Handler handler) {
        String sign = null;

        try {
            sign = getSignature(getHashMap59(), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_Lighting);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }


    /**
     * 获取特定语音列表
     *
     * @param handler
     */
    public static void getVoiceList(final Handler handler) {
        String sign = null;

        try {
            sign = getSignature(getHashMap58(), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_ArticleFixedList);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 模糊查询教练列表
     *
     * @param handler
     * @param Uname
     * @param pageNum
     * @param pageNumber
     */
    public static void getCoach_Titlte(final Handler handler, String Uname, int pageNum, int pageNumber) {

        String sign = null;

        try {
            sign = getSignature(getHashMap60(Uname, pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_Coach_Title);
        param.put("Uname", Uname);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 模糊查询视频列表
     *
     * @param handler
     * @param Vtitle
     * @param pageNum
     * @param pageNumber
     */
    public static void getVideo_Title(final Handler handler, String Vtitle, int pageNum, int pageNumber) {

        String sign = null;

        try {
            sign = getSignature(getHashMap61(Vtitle, pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_Video_Title);
        param.put("Vtitle", Vtitle);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 陪练商品列表 SparringList
     *
     * @param handler
     * @param pageNum
     * @param pageNumber
     */
    public static void getSparringList(final Handler handler, int pageNum, int pageNumber) {

        String sign = null;

        try {
            sign = getSignature(getHashMap62(pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_SparringList);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 陪练商品详情 SparringSingle
     *
     * @param handler
     * @param Id
     */
    public static void getSparringSingle(final Handler handler, String Id) {

        String sign = null;

        try {
            sign = getSignature(getHashMap63(Id), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_SparringSingle);
        param.put("Id", Id);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    public static void getUnitPriceList(final Handler handler, String PriceType) {
        String sign = null;

        try {
            sign = getSignature(getHashMap64(PriceType), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_UnitPriceList);
        param.put("PriceType", PriceType);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 陪练信息添加 Sp_ordered_Add
     *
     * @param handler
     * @param guid
     * @param OrdAddress
     * @param mg_address
     * @param licenseH
     * @param Telephone
     * @param Name
     * @param longitude
     * @param latitude
     * @param Sp_proID
     * @param LearnTime
     * @param Uid
     */
    public static void addSp_ordered_Add(final Handler handler, String guid, String OrdAddress, String mg_address, String licenseH,
                                         String Telephone, String Name, double longitude, double latitude, String Sp_proID, String LearnTime,
                                         String Uid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap65(guid, OrdAddress, mg_address, licenseH, Telephone, Name, longitude, latitude, Sp_proID, LearnTime, Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_Sp_ordered_Add);
        param.put("guid", guid);
        param.put("OrdAddress", OrdAddress);
        param.put("mg_address", mg_address);
        param.put("licenseH", licenseH);
        param.put("Telephone", Telephone);
        param.put("Name", Name);
        param.put("longitude", longitude);
        param.put("latitude", latitude);
        param.put("Sp_proID", Sp_proID);
        param.put("LearnTime", LearnTime);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 陪练信息添加 Sp_ordered_Add_nT
     *
     * @param handler
     * @param guid
     * @param mg_address
     * @param licenseH
     * @param Telephone
     * @param Name
     * @param longitude
     * @param latitude
     * @param Sp_proID
     * @param Uid
     */
    public static void addSp_ordered_Add_nT(final Handler handler, String guid, String mg_address, String licenseH,
                                            String Telephone, String Name, double longitude, double latitude, String Sp_proID,
                                            String Uid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap66(guid, mg_address, licenseH, Telephone, Name, longitude, latitude, Sp_proID, Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_Sp_ordered_Add_nT);
        param.put("guid", guid);
        param.put("mg_address", mg_address);
        param.put("licenseH", licenseH);
        param.put("Telephone", Telephone);
        param.put("Name", Name);
        param.put("longitude", longitude);
        param.put("latitude", latitude);
        param.put("Sp_proID", Sp_proID);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 教练陪练订单 SpOrders_Coach
     *
     * @param handler
     * @param Uid
     */
    public static void getSpOrders_Coach(final Handler handler, String Uid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap67(Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_SpOrders_Coach);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 我的陪练订单 SpOrdered_I
     *
     * @param handler
     * @param Uid
     */
    public static void getSpOrdered_I(final Handler handler, String Uid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap68(Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_SpOrdered_I);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 试驾 TestDrive_Add
     *
     * @param handler
     * @param Name
     * @param Phone
     */
    public static void textTestDrive_Add(final Handler handler, String Name, String Phone) {

        String sign = null;

        try {
            sign = getSignature(getHashMap69(Name, Phone), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_TestDrive_Add);
        param.put("Name", Name);
        param.put("Phone", Phone);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 试学 TryTolearn_Add
     *
     * @param handler
     * @param Name
     * @param Phone
     * @param Region
     * @param Detailed
     * @param DistriCoach
     */
    public static void testTryTolearn_Add(final Handler handler, String Name, String Phone, String
            Region, String Detailed, String DistriCoach) {

        String sign = null;

        try {
            sign = getSignature(getHashMap70(Name, Phone, Region, Detailed, DistriCoach), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_TryTolearn_Add);
        param.put("Name", Name);
        param.put("Phone", Phone);
        param.put("Region", Region);
        param.put("Detailed", Detailed);
        param.put("DistriCoach", DistriCoach);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 我的视频收藏 Collection_Sel
     *
     * @param handler
     * @param Uid
     * @param pageNum
     * @param pageNumber
     */
    public static void getCollection_Sel(final Handler handler, String Uid, int pageNum, int pageNumber) {

        String sign = null;

        try {
            sign = getSignature(getHashMap71(Uid, pageNum, pageNumber), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_Collection_Sel);
        param.put("Uid", Uid);
        param.put("pageNum", pageNum);
        param.put("pageNumber", pageNumber);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 教练认证附件添加 Coachfile_Add
     *
     * @param handler
     * @param Uid
     * @param PicSrc
     */
    public static void addCoachfile_Add(final Handler handler, String Uid, String PicSrc) {

        String sign = null;

        try {
            sign = getSignature(getHashMap72(Uid, PicSrc), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_Coachfile_Add);
        param.put("Uid", Uid);
        param.put("PicSrc", PicSrc);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 教练认证附件查看 Coachfile_Sel
     *
     * @param handler
     * @param Uid
     */
    public static void selCoachfile_Sel(final Handler handler, String Uid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap73(Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_Coachfile_Sel);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 用户个人信息 UserInformation
     *
     * @param handler
     * @param Uid
     */
    public static void getUserInformation(final Handler handler, String Uid) {

        String sign = null;

        try {
            sign = getSignature(getHashMap74(Uid), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_UserInformation);
        param.put("Uid", Uid);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 教练查看自己的试学 TryTolearn_ListDB
     *
     * @param handler
     * @param DistriCoach
     */
    public static void getTryTolearn_ListDB(final Handler handler, String DistriCoach) {

        String sign = null;

        try {
            sign = getSignature(getHashMap75(DistriCoach), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_TryTolearn_ListDB);
        param.put("DistriCoach", DistriCoach);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 教练试学状态修改 TryTolearn_EditState
     *
     * @param handler
     * @param ID
     * @param State
     */
    public static void editTryTolearn_EditState(final Handler handler, String ID, int State) {

        String sign = null;

        try {
            sign = getSignature(getHashMap76(ID, State), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_TryTolearn_EditState);
        param.put("ID", ID);
        param.put("State", State);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 订单 CouponTabAdd
     *
     * @param handler
     * @param WeChatName
     * @param WeChatPic
     * @param WeChatOpenid
     * @param Title
     * @param Amoney
     * @param OrderID
     * @param CouType
     */
    public static void addCouponTabAdd(final Handler handler, String WeChatName, String WeChatPic,
                                       String WeChatOpenid, String Title, String Amoney, String OrderID, String CouType) {

        String sign = null;

        try {
            sign = getSignature(getHashMap77(WeChatName, WeChatPic, WeChatOpenid, Title, Amoney, OrderID, CouType), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_CouponTabAdd);
        param.put("WeChatName", WeChatName);
        param.put("WeChatPic", WeChatPic);
        param.put("WeChatOpenid", WeChatOpenid);
        param.put("Title", Title);
        param.put("Amoney", Amoney);
        param.put("OrderID", OrderID);
        param.put("CouType", CouType);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 获取汽车品牌
     *
     * @param handler
     */
    public static void getParameterUsed(final Handler handler) {

        String sign = null;

        try {
            sign = getSignature(getHashMap78(), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_ParameterUsed);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 二手车列表 UsedCarList
     *
     * @param handler
     * @param Brand
     * @param Models
     * @param pageNumber
     * @param pageNum
     */
    public static void getUsedCarList(final Handler handler, String Brand, String Models, int pageNumber, int pageNum) {

        String sign = null;

        try {
            sign = getSignature(getHashMap79(Brand, Models, pageNumber, pageNum), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_UsedCarList);
        param.put("Brand", Brand);
        param.put("Models", Models);
        param.put("pageNumber", pageNumber);
        param.put("pageNum", pageNum);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 二手车详情 UsedCarTab_I
     *
     * @param handler
     * @param Car_UsedID
     */
    public static void getUsedCarTab_I(final Handler handler, String Car_UsedID) {

        String sign = null;

        try {
            sign = getSignature(getHashMap80(Car_UsedID), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_UsedCarTab_I);
        param.put("Car_UsedID", Car_UsedID);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

    /**
     * 二手车图片 UsedCarPicList
     *
     * @param handler
     * @param Car_UsedID
     */
    public static void getUsedCarPicList(final Handler handler, String Car_UsedID) {

        String sign = null;

        try {
            sign = getSignature(getHashMap81(Car_UsedID), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_UsedCarPicList);
        param.put("Car_UsedID", Car_UsedID);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });

    }

    /**
     * 砍价预约 E_UsedCarBargainAdd
     *
     * @param handler
     * @param Car_Name
     * @param Car_Telephone
     * @param Car_Amount
     * @param Car_UsedID
     */
    public static void kanE_UsedCarBargainAdd(final Handler handler, String Car_Name, String Car_Telephone, String Car_Amount, String Car_UsedID) {

        String sign = null;

        try {
            sign = getSignature(getHashMap82(Car_Name, Car_Telephone, Car_Amount, Car_UsedID), SIGNKEY);
        } catch (IOException e) {

            e.printStackTrace();
        }
        HttpParams param = new HttpParams();
        param.put("method", method_E_UsedCarBargainAdd);
        param.put("Car_Name", Car_Name);
        param.put("Car_Telephone", Car_Telephone);
        param.put("Car_Amount", Car_Amount);
        param.put("Car_UsedID", Car_UsedID);
        param.put("sign", sign);

        HttpClientUtils.getInstance().get(SERVER_ADDRESS, POST, param,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Message msg = new Message();
                        msg.what = SUCCESS_MSG;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String result, int statusCode,
                                          String errorResponse) {
                        Message msg = new Message();
                        msg.what = FAILED_MSG;
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                });
    }

}
