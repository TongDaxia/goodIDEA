<template>
    <div id="app">
        <no-found v-if="isEmpty"></no-found>
        <div class="content" v-if="tableData.length">
            <div class="head-div">
                <div class="head-div-left">
                    <span>报告期</span>
                </div>
                <div class="head-div-right">
                    <div class="head-div-right-scroll" id="right-title-panel">
                        <div class="head-div-right-box">
                            <span>总份额(万份)</span>
                            <span>本期申购(万份)</span>
                            <span>本期赎回(万份)</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="main-div" id="mainContent">
                <div class="main-div-left">
                    <ul id="left-ul">
                        <li v-for="d in tableData">{{d.reportDate}}</li>
                    </ul>
                </div>
                <div class="main-div-right">
                    <div class="main-div-right-scroll" id="right-panel">
                        <div class="main-div-right-box">
                            <ul id="right-ul">
                                <li v-for="d in tableData">
                                    <span>{{d.total}}</span>
                                    <span>{{d.inNum}}</span>
                                    <span>{{d.outNum}}</span>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <!--页面底部status：true:正在加载 false：没有更多数据-->
                <bottom-status v-show="isLoading && hasMore" :status="true"></bottom-status>
                <bottom-status v-show="!hasMore && !isEmpty && isFullScreen" :status="false"></bottom-status>
            </div>
        </div>
    </div>
</template>

<script>
    import common from '@utils/commons';
    import noFound from '../../components/noFound';
    import bottomStatus from '../../components/bottomStatus';

    const param = common.getParams();

    export default {
        data() {
            return {
                tableData:[],
                curPageSize: 15,//当前页面每页展示条数
                isEmpty: false,
                isLoading: false,//是否正在加载
                bottomHeight: 200,//内容距离底部距离
                hasMore: false,//是否有更多数据
                isFullScreen: false, //内容是否铺满屏幕，到达页面底部
                scrollFrom: '',//滚动的面板
            }
        },
        created () {
            this.loadData()
        },
        mounted() {
            common.resize();
            common.bindScrollEvt();
            this.$nextTick(() => {
                common.addWaterMark();
            });
        },
        methods: {
            // 初次加载，页面初始化
            loadData () {
                this.$loading.open();
                const req ={...param, skip: 0, pagesize: this.curPageSize};
                this.$get('/finchinaAPP/nonStandard/getShareChangeDetail.action', req).then(res => {
                    this.$loading.close();
                    this.hasMore = res.data.hasMore === 1;
                    this.tableData = res.data.list;
                    //左右表格高度统一
                    this.initTableLayout();
                    // 若内容未到底部，判断加载完是否到底部
                    if (this.hasMore){
                        this.autoGet();//数据未满屏，自动请求下一页
                    }else {
                        this.ifFullScreen();
                    }
                    //数据加载完成后，添加滚动事件
                    this.$nextTick(()=>{
                        $("#right-title-panel").on('touchstart', e => {
                            this.scrollFrom = 'scrollTitle';
                        });
                        $("#right-panel").on('touchstart', e => {
                            this.scrollFrom = 'scrollPanel';
                        });
                        //右侧头部横向滚动时，同步滚动内容
                        $("#right-title-panel").scroll(() => {
                            if (this.scrollFrom === 'scrollPanel') return;
                            $("#right-panel").scrollLeft($("#right-title-panel").scrollLeft());
                        });
                        //右侧内容横向滚动时，同步滚动头部内容
                        $("#right-panel").scroll(() => {
                            if (this.scrollFrom === 'scrollTitle') return;
                            $("#right-title-panel").scrollLeft($("#right-panel").scrollLeft());
                        });
                        $("#mainContent").on('touchmove', () => {
                            this.autoGet();
                        })
                    })
                }).catch(err => {
                    this.$loading.close();
                    this.isEmpty = true;
                    this.hasMore = false;
                    this.isFullScreen = false;
                });
            },
            //左右表格高度统一
            initTableLayout () {
                this.$nextTick(()=>{
                    const leftTr = document.getElementById("left-ul").getElementsByTagName('li'),//表格左一列
                        rightTr = document.getElementById("right-ul").getElementsByTagName('li');//表格右三列
                    for (let j = 0,l = rightTr.length; j < l; j++) {
                        //js会对其自动四舍五入，导致计算出现偏差
                        const hei = Math.ceil(rightTr[j].clientHeight || 0) + 'px';
                        leftTr[j].style.height = hei;
                        rightTr[j].style.height = hei;
                    }
                    //遮盖横向滚动条
                    document.querySelector(".main-div-right").style.height =
                        document.querySelector(".main-div-right-scroll").clientHeight - 7 +'px';
                })
            },
            //获取下一页数据
            getNestData(){
                this.isLoading = true;
                const req ={...param, skip: this.tableData.length, pagesize: this.curPageSize};
                this.$get('/finchinaAPP/nonStandard/getShareChangeDetail.action', req).then(res => {
                    this.tableData.push(...res.data.list);
                    this.hasMore = res.data.hasMore === 1;
                    this.isLoading = false;
                    //左右表格高度统一
                    this.initTableLayout();
                    // 若内容未到底部，判断加载完是否到底部
                    if (!this.isFullScreen){
                        this.ifFullScreen();
                    }
                }).catch(err=>{
                    this.hasMore = false;
                    this.isLoading = false;
                    // 若内容未到底部，判断加载完是否到底部
                    if (!this.isFullScreen){
                        this.ifFullScreen();
                    }
                });
            },
            //根据屏幕高度判断是否需要再次加载数据
            autoGet(){
                this.$nextTick(() => {
                    if (this.hasMore && !this.isLoading){
                        let t = document.querySelector(".main-div");
                        //滚动条滚到底部时：窗口高度+滚动条高度 = 内容区高度
                        let scrollTop = t.scrollTop,//滚动条高度
                            wHeight = t.clientHeight,//窗口高度
                            conHeight = t.scrollHeight;//内容高度

                        //在还有200px滚到底部之前加载下一页
                        let isScrollBottom = conHeight - wHeight - scrollTop <= this.bottomHeight;
                        if (isScrollBottom) {
                            this.getNestData();
                        }
                    }
                })
            },
            //内容是否铺满屏幕
            ifFullScreen(){
                this.$nextTick(() => {
                    //内容区高度 > 窗口高度 表示窗口已铺满
                    let wHeight = document.querySelector(".content").scrollHeight -
                        document.querySelector(".head-div").scrollHeight,//窗口高度
                        conHeight = document.querySelector(".main-div").scrollHeight;//内容高度
                    this.isFullScreen = wHeight - conHeight <= 100;
                })
            },
        },
        components: {
            noFound, bottomStatus
        },
    }
</script>

<style lang="less">
    @import "../../assets/less/base";
    #app {
        height: 100%;
        .content{
            height: 100%;
            overflow-y: hidden;
            .head-div{
                height: 0.33rem;
                position: relative;
                padding: 0.34rem 0.32rem 0.32rem;
                font-family: PingFangSC-Medium;
                font-weight: bolder;
                font-size: 0.26rem;
                color: #333333;
                display: flex;
                .head-div-left{
                    width: 2.2rem;
                }
                .head-div-right{
                    width: calc(100% - 2.2rem);
                    height: 0.3rem;
                    overflow: hidden;
                    .head-div-right-scroll{
                        width: 100%;
                        height: 0.5rem;
                        overflow-x: scroll;
                        position: relative;
                        -webkit-overflow-scrolling: touch;
                        .head-div-right-box{
                            display: inline-flex;
                            text-align: right;
                            span{
                                width: 2.33rem;
                            }
                        }
                    }
                    &:before,&:after{
                        content: '';
                        top:0.38rem;
                        position: absolute;
                        display: inline-block;
                        border: 0.1rem solid transparent;
                    }
                    &:before{
                        border-right-color: #8c8c8c;
                        left: 2.2rem;
                        z-index: 1;
                    }
                    &:after{
                        border-left-color: #8c8c8c;
                        right:0;
                    }
                }
            }
            .main-div{
                align-items: flex-start;
                height: auto;
                max-height: calc(100% - 1rem);
                padding: 0 0.32rem;
                position: relative;
                font-family: PingFangSC-Regular;
                font-size: 0.26rem;
                color: #111111;
                display: flex;
                flex-wrap: wrap;
                overflow-y: scroll;
                -webkit-overflow-scrolling: touch;
                .main-div-left{
                    width: 2.2rem;
                    li{
                        margin-bottom: 0.3rem;
                        text-align: left;
                        display: inline-flex;
                        width: 100%;
                    }
                }
                .main-div-right{
                    width: calc(100% - 2.2rem);
                    overflow: hidden;
                    .main-div-right-scroll{
                        width: 100%;
                        overflow-x: scroll;
                        position: relative;
                        -webkit-overflow-scrolling: touch;
                        .main-div-right-box{
                            /*display: inline-flex;*/
                            span{
                                width: 2.33rem;
                                word-break: break-all;
                            }
                            li{
                                display: inline-flex;
                                text-align: right;
                                margin-bottom: 0.3rem;
                            }
                        }
                    }
                }
                .bottom-msg{
                    width: 100%;
                    margin: 0 -0.32rem;
                    padding-top: 0.48rem
                }
            }
        }
    }
</style>
