package com.honestwalker.android.APICore.API.bean;

import java.io.Serializable;

/**
 * Created by honestwalker on 15-8-24.
 */
public class VersionInfoBean implements Serializable {

    private String version;

    private String url;

    private AppVersionBean app_version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AppVersionBean getApp_version() {
        return app_version;
    }

    public void setApp_version(AppVersionBean app_version) {
        this.app_version = app_version;
    }


    public static class AppVersionBean {
        private String version;
        private String description;
        private String download_url;
        private boolean required;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }
    }
}
