package com.xb.soft.net.mode;

import java.util.List;

public class ConfigBean {

    /**
     * sysType : android
     * core : {"name":"新宝科技","packageName":"com.xb.soft","version":"1.0.0","sequence":"0e45a5f36477446d8123a5f12b1896c7","logo":"http://view1.gg-future.com/apppacktest/icon_180x180.png","startInfo":{"horizonImg":"http://view1.gg-future.com/apppacktest/horizon.jpg","verticalImg":"http://view1.gg-future.com/apppacktest/vertical.jpg","autoClose":false,"maxDisplayTime":5},"verticalScreen":-1,"sideslip":1,"urlInfo":{"urlType":2,"urlTxt":"http://domainspick.net/jc/domain_list_app.txt","urls":[],"checkType":2},"versionCheck":{"urlType":2,"urlTxt":"http://domainspick.net/jc/domain_list_app.txt","urls":[],"checkType":1}}
     * extend : {"statusBar":{"show":1,"showInfo":{"showType":1,"fontColor":2,"background":"0e132b"}},"guide":{"urls":[]},"cache":0,"exit":0,"notification":{"checkType":0,"pushType":"0","appKey":""},"tripartite":0}
     */

    private String sysType;
    private CoreBean core;
    private ExtendBean extend;

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }

    public CoreBean getCore() {
        return core;
    }

    public void setCore(CoreBean core) {
        this.core = core;
    }

    public ExtendBean getExtend() {
        return extend;
    }

    public void setExtend(ExtendBean extend) {
        this.extend = extend;
    }

    public static class CoreBean {
        /**
         * name : 新宝科技
         * packageName : com.xb.soft
         * version : 1.0.0
         * sequence : 0e45a5f36477446d8123a5f12b1896c7
         * logo : http://view1.gg-future.com/apppacktest/icon_180x180.png
         * startInfo : {"horizonImg":"http://view1.gg-future.com/apppacktest/horizon.jpg","verticalImg":"http://view1.gg-future.com/apppacktest/vertical.jpg","autoClose":false,"maxDisplayTime":5}
         * verticalScreen : -1
         * sideslip : 1
         * urlInfo : {"urlType":2,"urlTxt":"http://domainspick.net/jc/domain_list_app.txt","urls":[],"checkType":2}
         * versionCheck : {"urlType":2,"urlTxt":"http://domainspick.net/jc/domain_list_app.txt","urls":[],"checkType":1}
         */

        private String name;
        private String packageName;
        private String version;
        private String sequence;
        private String logo;
        private StartInfoBean startInfo;
        private int verticalScreen;
        private int sideslip;
        private UrlInfoBean urlInfo;
        private VersionCheckBean versionCheck;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public StartInfoBean getStartInfo() {
            return startInfo;
        }

        public void setStartInfo(StartInfoBean startInfo) {
            this.startInfo = startInfo;
        }

        public int getVerticalScreen() {
            return verticalScreen;
        }

        public void setVerticalScreen(int verticalScreen) {
            this.verticalScreen = verticalScreen;
        }

        public int getSideslip() {
            return sideslip;
        }

        public void setSideslip(int sideslip) {
            this.sideslip = sideslip;
        }

        public UrlInfoBean getUrlInfo() {
            return urlInfo;
        }

        public void setUrlInfo(UrlInfoBean urlInfo) {
            this.urlInfo = urlInfo;
        }

        public VersionCheckBean getVersionCheck() {
            return versionCheck;
        }

        public void setVersionCheck(VersionCheckBean versionCheck) {
            this.versionCheck = versionCheck;
        }

        public static class StartInfoBean {
            /**
             * horizonImg : http://view1.gg-future.com/apppacktest/horizon.jpg
             * verticalImg : http://view1.gg-future.com/apppacktest/vertical.jpg
             * autoClose : false
             * maxDisplayTime : 5
             */

            private String horizonImg;
            private String verticalImg;
            private boolean autoClose;
            private int maxDisplayTime;

            public String getHorizonImg() {
                return horizonImg;
            }

            public void setHorizonImg(String horizonImg) {
                this.horizonImg = horizonImg;
            }

            public String getVerticalImg() {
                return verticalImg;
            }

            public void setVerticalImg(String verticalImg) {
                this.verticalImg = verticalImg;
            }

            public boolean isAutoClose() {
                return autoClose;
            }

            public void setAutoClose(boolean autoClose) {
                this.autoClose = autoClose;
            }

            public int getMaxDisplayTime() {
                return maxDisplayTime;
            }

            public void setMaxDisplayTime(int maxDisplayTime) {
                this.maxDisplayTime = maxDisplayTime;
            }
        }

        public static class UrlInfoBean {
            /**
             * urlType : 2
             * urlTxt : http://domainspick.net/jc/domain_list_app.txt
             * urls : []
             * checkType : 2
             */

            private int urlType;
            private String urlTxt;
            private int checkType;
            private List<String> urls;

            public int getUrlType() {
                return urlType;
            }

            public void setUrlType(int urlType) {
                this.urlType = urlType;
            }

            public String getUrlTxt() {
                return urlTxt;
            }

            public void setUrlTxt(String urlTxt) {
                this.urlTxt = urlTxt;
            }

            public int getCheckType() {
                return checkType;
            }

            public void setCheckType(int checkType) {
                this.checkType = checkType;
            }

            public List<String> getUrls() {
                return urls;
            }

            public void setUrls(List<String> urls) {
                this.urls = urls;
            }
        }

        public static class VersionCheckBean {
            /**
             * urlType : 2
             * urlTxt : http://domainspick.net/jc/domain_list_app.txt
             * urls : []
             * checkType : 1
             */

            private int urlType;
            private String urlTxt;
            private int checkType;
            private List<String> urls;

            public int getUrlType() {
                return urlType;
            }

            public void setUrlType(int urlType) {
                this.urlType = urlType;
            }

            public String getUrlTxt() {
                return urlTxt;
            }

            public void setUrlTxt(String urlTxt) {
                this.urlTxt = urlTxt;
            }

            public int getCheckType() {
                return checkType;
            }

            public void setCheckType(int checkType) {
                this.checkType = checkType;
            }

            public List<String> getUrls() {
                return urls;
            }

            public void setUrls(List<String> urls) {
                this.urls = urls;
            }
        }
    }

    public static class ExtendBean {
        /**
         * statusBar : {"show":1,"showInfo":{"showType":1,"fontColor":2,"background":"0e132b"}}
         * guide : {"urls":[]}
         * cache : 0
         * exit : 0
         * notification : {"checkType":0,"pushType":"0","appKey":""}
         * tripartite : 0
         */

        private StatusBarBean statusBar;
        private GuideBean guide;
        private int cache;
        private int exit;
        private NotificationBean notification;
        private int tripartite;

        public StatusBarBean getStatusBar() {
            return statusBar;
        }

        public void setStatusBar(StatusBarBean statusBar) {
            this.statusBar = statusBar;
        }

        public GuideBean getGuide() {
            return guide;
        }

        public void setGuide(GuideBean guide) {
            this.guide = guide;
        }

        public int getCache() {
            return cache;
        }

        public void setCache(int cache) {
            this.cache = cache;
        }

        public int getExit() {
            return exit;
        }

        public void setExit(int exit) {
            this.exit = exit;
        }

        public NotificationBean getNotification() {
            return notification;
        }

        public void setNotification(NotificationBean notification) {
            this.notification = notification;
        }

        public int getTripartite() {
            return tripartite;
        }

        public void setTripartite(int tripartite) {
            this.tripartite = tripartite;
        }

        public static class StatusBarBean {
            /**
             * show : 1
             * showInfo : {"showType":1,"fontColor":2,"background":"0e132b"}
             */

            private int show;
            private ShowInfoBean showInfo;

            public int getShow() {
                return show;
            }

            public void setShow(int show) {
                this.show = show;
            }

            public ShowInfoBean getShowInfo() {
                return showInfo;
            }

            public void setShowInfo(ShowInfoBean showInfo) {
                this.showInfo = showInfo;
            }

            public static class ShowInfoBean {
                /**
                 * showType : 1
                 * fontColor : 2
                 * background : 0e132b
                 */

                private int showType;
                private int fontColor;
                private String background;

                public int getShowType() {
                    return showType;
                }

                public void setShowType(int showType) {
                    this.showType = showType;
                }

                public int getFontColor() {
                    return fontColor;
                }

                public void setFontColor(int fontColor) {
                    this.fontColor = fontColor;
                }

                public String getBackground() {
                    return background;
                }

                public void setBackground(String background) {
                    this.background = background;
                }
            }
        }

        public static class GuideBean {
            private List<?> urls;

            public List<?> getUrls() {
                return urls;
            }

            public void setUrls(List<?> urls) {
                this.urls = urls;
            }
        }

        public static class NotificationBean {
            /**
             * checkType : 0
             * pushType : 0
             * appKey :
             */

            private int checkType;
            private String pushType;
            private String appKey;

            public int getCheckType() {
                return checkType;
            }

            public void setCheckType(int checkType) {
                this.checkType = checkType;
            }

            public String getPushType() {
                return pushType;
            }

            public void setPushType(String pushType) {
                this.pushType = pushType;
            }

            public String getAppKey() {
                return appKey;
            }

            public void setAppKey(String appKey) {
                this.appKey = appKey;
            }
        }
    }
}
