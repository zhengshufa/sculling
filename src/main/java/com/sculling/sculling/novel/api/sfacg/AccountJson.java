package com.sculling.sculling.novel.api.sfacg;
import static com.sculling.sculling.novel.api.sfacg.ChapListJson.Status;

public class AccountJson {

    public Status   status;

    public Data     data;

    public static class Data {
        public String   avatar;
        public long     accountId;
        public String   userName;
        public boolean  isAuthor;
        public String   registerDate;
        public String   roleName;
        public String   phoneNum;
        public int      countryCode;
        public String   email;
        public String   nickName;
        public int      fireCoin;
    }
}
