package com.xiaolian.amigo.data.network.model.bathroom;

import java.util.List;

public class BathBuildingRespDTO {

        /**
         * buildingName : string
         * floors : [{"floorName":"string","groups":[{"bathRooms":[{"deviceNo":0,"id":0,"name":"string","status":0,"xaxis":0,"yaxis":0}],"displayName":"string","groupId":0,"groupName":"string"}]}]
         * methods : [0]
         * missTimes : 0
         */

        private String buildingName;
        private int missTimes;
        private java.util.List<FloorsBean> floors;
        private java.util.List<Integer> methods;

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

        public int getMissTimes() {
            return missTimes;
        }

        public void setMissTimes(int missTimes) {
            this.missTimes = missTimes;
        }

        public List<FloorsBean> getFloors() {
            return floors;
        }

        public void setFloors(List<FloorsBean> floors) {
            this.floors = floors;
        }

        public List<Integer> getMethods() {
            return methods;
        }

        public void setMethods(List<Integer> methods) {
            this.methods = methods;
        }

        public static class FloorsBean {
            /**
             * floorName : string
             * groups : [{"bathRooms":[{"deviceNo":0,"id":0,"name":"string","status":0,"xaxis":0,"yaxis":0}],"displayName":"string","groupId":0,"groupName":"string"}]
             */

            private String floorName;
            private java.util.List<GroupsBean> groups;

            private float left ;   //  每层的左边坐标
            private float bottom ;  //  每层的底部坐标

            private float Width ;

            public float getWidth() {
                return Width;
            }

            public void setWidth(float width) {
                Width = width;
            }

            public float getLeft() {
                return left;
            }

            public void setLeft(float left) {
                this.left = left;
            }

            public float getBottom() {
                return bottom;
            }

            public void setBottom(float bottom) {
                this.bottom = bottom;
            }

            private float height ;  //每层的高度  用于更好的计算位置坐标

            public float getHeight() {
                return height;
            }

            public void setHeight(float height) {
                this.height = height;
            }

            public String getFloorName() {
                return floorName;
            }

            public void setFloorName(String floorName) {
                this.floorName = floorName;
            }

            public List<GroupsBean> getGroups() {
                return groups;
            }

            public void setGroups(List<GroupsBean> groups) {
                this.groups = groups;
            }

            public static class GroupsBean {
                /**
                 * bathRooms : [{"deviceNo":0,"id":0,"name":"string","status":0,"xaxis":0,"yaxis":0}]
                 * displayName : string
                 * groupId : 0
                 * groupName : string
                 */

                private String displayName;
                private int groupId;
                private String groupName;
                private java.util.List<BathRoomsBean> bathRooms;

                private float borderTextToBathroom ;  //  每组的底部坐标到浴室块的距离

                private float left ;  //  每组的左边坐标

                private float bottom ;  //  每组的底部坐标

                private float width ;   //  每组的宽度  便于计算列的位置 ， 这个宽度有可能是房间矩形的宽度，也有可能是下面字体的宽，取最大值

                private float rectWidth ;  //  每组矩形的宽度

                private float  maxX ;  //  组的最大行数

                private float maxY ;    //  组的最大列数


                public float getBorderTextToBathroom() {
                    return borderTextToBathroom;
                }

                public void setBorderTextToBathroom(float borderTextToBathroom) {
                    this.borderTextToBathroom = borderTextToBathroom;
                }

                public float getLeft() {
                    return left;
                }

                public void setLeft(float left) {
                    this.left = left;
                }

                public float getBottom() {
                    return bottom;
                }

                public void setBottom(float bottom) {
                    this.bottom = bottom;
                }

                public float getRectWidth() {
                    return rectWidth;
                }

                public void setRectWidth(float rectWidth) {
                    this.rectWidth = rectWidth;
                }

                public float getWidth() {
                    return width;
                }

                public void setWidth(float width) {
                    this.width = width;
                }

                public String getDisplayName() {
                    return displayName;
                }

                public void setDisplayName(String displayName) {
                    this.displayName = displayName;
                }

                public int getGroupId() {
                    return groupId;
                }

                public void setGroupId(int groupId) {
                    this.groupId = groupId;
                }

                public String getGroupName() {
                    return groupName;
                }

                public void setGroupName(String groupName) {
                    this.groupName = groupName;
                }

                public List<BathRoomsBean> getBathRooms() {
                    return bathRooms;
                }

                public void setBathRooms(List<BathRoomsBean> bathRooms) {
                    this.bathRooms = bathRooms;
                }

                public float getMaxX() {
                    return maxX;
                }

                public void setMaxX(float maxX) {
                    this.maxX = maxX;
                }

                public float getMaxY() {
                    return maxY;
                }

                public void setMaxY(float maxY) {
                    this.maxY = maxY;
                }

                public static class BathRoomsBean {
                    /**
                     * deviceNo : 0
                     * id : 0
                     * name : string
                     * status : 0
                     * xaxis : 0
                     * yaxis : 0
                     */

                    private long deviceNo;
                    private long id;
                    private String name;
                    private int status;
                    private int xaxis;
                    private int yaxis;

                    private String roomName ;

                    private float left ;  //  left坐标
                    private float top ;   //  top坐标
                    private float right ;  // right坐标
                    private float bottom;  // bottom坐标

                    public String getRoomName() {
                        return roomName;
                    }

                    public void setRoomName(String roomName) {
                        this.roomName = roomName;
                    }

                    public float getLeft() {
                        return left;
                    }

                    public void setLeft(float left) {
                        this.left = left;
                    }

                    public float getTop() {
                        return top;
                    }

                    public void setTop(float top) {
                        this.top = top;
                    }

                    public float getRight() {
                        return right;
                    }

                    public void setRight(float right) {
                        this.right = right;
                    }

                    public float getBottom() {
                        return bottom;
                    }

                    public void setBottom(float bottom) {
                        this.bottom = bottom;
                    }

                    public long getDeviceNo() {
                        return deviceNo;
                    }

                    public void setDeviceNo(long deviceNo) {
                        this.deviceNo = deviceNo;
                    }

                    public long getId() {
                        return id;
                    }

                    public void setId(long id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public int getXaxis() {
                        return xaxis;
                    }

                    public void setXaxis(int xaxis) {
                        this.xaxis = xaxis;
                    }

                    public int getYaxis() {
                        return yaxis;
                    }

                    public void setYaxis(int yaxis) {
                        this.yaxis = yaxis;
                    }
                }
            }
        }
}
