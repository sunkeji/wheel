package com.skj.wheel.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 孙科技 on 2017/7/6.
 */

public class LocalAreaBean implements Serializable {
    private List<RootBean> root;

    public List<RootBean> getRoot() {
        return root;
    }

    public void setRoot(List<RootBean> root) {
        this.root = root;
    }

    public static class RootBean implements Serializable {
        /**
         * id : 1
         * provinceid : 110000
         * province : 北京市
         * cities : [{"id":1,"cityid":"110100","city":"市辖区","areas":[{"id":1,"areaid":"110101","area":"东城区"},{"id":2,"areaid":"110102","area":"西城区"},{"id":3,"areaid":"110103","area":"崇文区"},{"id":4,"areaid":"110104","area":"宣武区"},{"id":5,"areaid":"110105","area":"朝阳区"},{"id":6,"areaid":"110106","area":"丰台区"},{"id":7,"areaid":"110107","area":"石景山区"},{"id":8,"areaid":"110108","area":"海淀区"},{"id":9,"areaid":"110109","area":"门头沟区"},{"id":10,"areaid":"110111","area":"房山区"},{"id":11,"areaid":"110112","area":"通州区"},{"id":12,"areaid":"110113","area":"顺义区"},{"id":13,"areaid":"110114","area":"昌平区"},{"id":14,"areaid":"110115","area":"大兴区"},{"id":15,"areaid":"110116","area":"怀柔区"},{"id":16,"areaid":"110117","area":"平谷区"}]},{"id":2,"cityid":"110200","city":"县","areas":[{"id":17,"areaid":"110228","area":"密云县"},{"id":18,"areaid":"110229","area":"延庆县"}]}]
         */

        private int id;
        private Integer provinceid;
        private String province;
        private List<CitiesBean> cities;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Integer getProvinceid() {
            return provinceid;
        }

        public void setProvinceid(Integer provinceid) {
            this.provinceid = provinceid;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public List<CitiesBean> getCities() {
            return cities;
        }

        public void setCities(List<CitiesBean> cities) {
            this.cities = cities;
        }

        public static class CitiesBean implements Serializable {
            /**
             * id : 1
             * cityid : 110100
             * city : 市辖区
             * areas : [{"id":1,"areaid":"110101","area":"东城区"},{"id":2,"areaid":"110102","area":"西城区"},{"id":3,"areaid":"110103","area":"崇文区"},{"id":4,"areaid":"110104","area":"宣武区"},{"id":5,"areaid":"110105","area":"朝阳区"},{"id":6,"areaid":"110106","area":"丰台区"},{"id":7,"areaid":"110107","area":"石景山区"},{"id":8,"areaid":"110108","area":"海淀区"},{"id":9,"areaid":"110109","area":"门头沟区"},{"id":10,"areaid":"110111","area":"房山区"},{"id":11,"areaid":"110112","area":"通州区"},{"id":12,"areaid":"110113","area":"顺义区"},{"id":13,"areaid":"110114","area":"昌平区"},{"id":14,"areaid":"110115","area":"大兴区"},{"id":15,"areaid":"110116","area":"怀柔区"},{"id":16,"areaid":"110117","area":"平谷区"}]
             */

            private int id;
            private Integer cityid;
            private String city;
            private List<AreasBean> areas;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Integer getCityid() {
                return cityid;
            }

            public void setCityid(Integer cityid) {
                this.cityid = cityid;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public List<AreasBean> getAreas() {
                return areas;
            }

            public void setAreas(List<AreasBean> areas) {
                this.areas = areas;
            }

            public static class AreasBean implements Serializable {
                public AreasBean(String area, Integer areaid) {
                    this.area = area;
                    this.areaid = areaid;
                }

                /**
                 * id : 1
                 * areaid : 110101
                 * area : 东城区
                 */

                private int id;
                private Integer areaid;
                private String area;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public Integer getAreaid() {
                    return areaid;
                }

                public void setAreaid(Integer areaid) {
                    this.areaid = areaid;
                }

                public String getArea() {
                    return area;
                }

                public void setArea(String area) {
                    this.area = area;
                }
            }
        }
    }
}
