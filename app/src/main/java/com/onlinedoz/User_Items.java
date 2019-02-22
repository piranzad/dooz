package com.onlinedoz;

public class User_Items {
// دخیره اسم فای های پی اچ پی

   private String ItemsName, ItemsUsername, ItemsAge,ItemsPassword,Itemsimage,Itemsonline;
    //public static String HOST_NAME = "";
    public static String HOST_NAME = "http://192.168.43.150/server/";
    public static String LOGIN = "login.php";
    public static String ADDUSER = "adduser.php";
    public static String UPLOADIMG = "uploadimg.php";
    public static String UPLOAD = "upload.php";
    public static String SETONLINE = "setonline.php";
    public static String CHECKONLINE = "checkonline.php";
    public static String CHECKLASTONLINE = "checklastonline.php";
    public static String EDITPROFILE = "editprofil.php";
    public static String SHOWUSERINFO = "showUserInfo.php";
    public static String ADDMSG = "addmsg.php";
    public static String GETCOMMENTS = "get_comments.php";
    public static String GAME_R = "game_r.php";
    public static String CHECK_GAME_R = "check_game_r.php";
    public static String GAME_R_REPLY = "game_r_reply.php";
    public static String GET_HAREKAT = "get_harekat.php";
    public static String CHECK_ADMIN = "check_admin.php";
    public static String DELETE_MSG = "delete_msg.php";
    public static String CONFIRM_MSG = "confirm_msg.php";
    public static String CREATE_TBL = "create_tbl.php";
    public static String SEND_MSG = "send_msg.php";
    public static String GET_MSG = "get_msg.php";
    public static String SET_BLOCK = "set_block.php";
    public static String SET_IMAGE = "set_image.php";
    public static String COUNT_MSG = "count_msg.php";

// این ایتم ها ربطی به ایت های کلاس ایتم ندارد
    public User_Items(String itemsName, String itemsUsername, String itemsAge, String itemsPassword, String itemsimage, String itemsonline) {
        ItemsName = itemsName;
        ItemsUsername = itemsUsername;
        ItemsAge = itemsAge;
        ItemsPassword = itemsPassword;
        Itemsimage = itemsimage;
        Itemsonline = itemsonline;

    }
    // متد های ست را خواستی پاک کن اگر خواستی

    public String getItemsName() {
        return ItemsName;
    }

    public void setItemsName(String itemsName) {
        ItemsName = itemsName;
    }

    public String getItemsUsername() {
        return ItemsUsername;
    }

    public void setItemsUsername(String itemsUsername) {
        ItemsUsername = itemsUsername;
    }

    public String getItemsAge() {
        return ItemsAge;
    }

    public void setItemsAge(String itemsAge) {
        ItemsAge = itemsAge;
    }

    public String getItemsPassword() {
        return ItemsPassword;
    }

    public void setItemsPassword(String itemsPassword) {
        ItemsPassword = itemsPassword;
    }

    public String getItemsimage() {
        return Itemsimage;
    }

    public void setItemsimage(String itemsimage) {
        Itemsimage = itemsimage;
    }

    public String getItemsonline() {
        return Itemsonline;
    }

    public void setItemsonline(String itemsonline) {
        Itemsonline = itemsonline;
    }
}

