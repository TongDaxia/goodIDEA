<template>
    <div class="content">
        <div v-show="showTimeDown" class="menu-cover" @touchmove.prevent @click="showTimeDown=false"></div>
        <no-found v-if="isEmpty"></no-found>
        <div class="main-box">
            <div class="end-time" v-if="timeOption[0]">报告期：
                <span class="time-option" @click=" showTimeDown = !showTimeDown;"
                      :class="{ 'menu-active': showTimeDown}">{{showTime}}</span>
            </div>
            <!--时间下拉选项-->
            <div v-show="showTimeDown" class="menu-cover" @touchmove.prevent @click.self="showTimeDown = false">
                <transition name="slide-down">
                    <ul v-show="showTimeDown" v-scroll-fix>
                        <li v-for="v in timeOption"
                            @click="selecteTime(v)"
                            class="needsclick"
                            :class="{'menu-selected':v.key === selTime}">{{v.value}}
                        </li>
                    </ul>
                </transition>
            </div>
            <div class="main" v-if="tableData.length" :id="`mainContent${type}`">
                <table class="scroll-tbody">
                    <thead>
                    <tr>
                        <th v-for="t in title">{{t}}</th>
                    </tr>
                    </thead>
                    <tbody :class="`tbody${type}`">
                    <tr v-for="d in tableData">
                        <td v-for="v in d">{{v}}</td>
                    </tr>
                    <!--页面底部status：true:正在加载 false：没有更多数据-->
                    <bottom-status v-show="isLoading && hasMore" :status="true"></bottom-status>
                    <bottom-status v-show="!hasMore && !isEmpty && isFullScreen" :status="false"></bottom-status>
                    </tbody>
                </table>
            </div>
        </div>
        <!-- 初始化遮罩层 -->
        <div class="indicator" v-show="maskVisible">
            <div class="indicator-wrapper">
                <span class="indicator-spin"></span>
                <span class="indicator-text">正在加载...</span>
            </div>
            <div class="indicator-mask" @touchmove.stop.prevent  @scroll.stop.prevent></div>
        </div>
    </div>
</template>

<script>
    import common from '@utils/commons';
    import noFound from '../../../components/noFound'
    import scrollFix from '../../../directors/scrollFix';
    import bottomStatus from '../../../components/bottomStatus';

    let params = common.getParams();

    export default {
        props: ['type'],
        data() {
            return {
                maskVisible: false,//是否显示遮罩
                timeOption: [],//时间下拉框选项
                showTimeDown: false,//时间下拉框显示隐藏
                selTime: '',//选中的时间项
                showTime: '',//展示的时间项
                tableData:[],//当前展示的数据列表
                title: [],//标题
                curPageSize: 15,//当前页面每页展示条数
                isLoading: false,//是否正在加载
                noPromise: false,
                bottomHeight: 200,
                isEmpty: false,
                hasMore: false,//是否有更多数据
                isFullScreen: false //内容是否铺满屏幕，到达页面底部
            }
        },
        created () {
            this.selTime = decodeURIComponent(params.reportDate).split(",")[this.type];
            if (this.selTime) {
                this.getTime();
                this.loadData();
             }else {
                this.isEmpty = true;
                this.hasMore = false;
                this.isFullScreen = false;
            }
        },
        methods: {
            //获取报告期列表
            getTime(){
                const req ={...params, type: this.type};
                this.$get('/finchinaAPP/nonStandard/getInvestmentPortfolioDateList.action', req).then(res => {
                    this.timeOption = res.data;
                    this.timeOption.map(d=>{
                        if (d.key === this.selTime){
                            this.showTime = d.value;
                        }
                    })
                }).catch(err => {
                    this.isEmpty = true;
                    this.hasMore = false;
                    this.isFullScreen = false;
                });
            },
            //报告期选择
            selecteTime(v) {
                this.selTime = v.key;
                this.showTime = v.value;
                this.showTimeDown = false;
                this.loadData();
            },
            // 初次加载，页面初始化
            loadData () {
                this.maskVisible = true;
                const req ={...params, skip: 0, pagesize: this.curPageSize, type: this.type, reportDate:this.selTime};
                this.$get('/finchinaAPP/nonStandard/getInvestmentPortfolioDetail.action', req).then(res => {
                    this.maskVisible = false;
                    this.hasMore = res.data.hasMore === 1;
                    this.title = res.data.title;
                    this.tableData = res.data.list;
                    if (this.hasMore){
                        this.autoGet();//数据未满屏，自动请求下一页
                    }else{
                        // 判断加载完是否到底部
                        this.ifFullScreen();
                    }
                    this.$nextTick(()=>{
                        $("#" + `mainContent${this.type}`).on('touchmove', () => {
                            this.autoGet();
                        })
                    })
                }).catch(err => {
                    this.maskVisible = false;
                    this.isEmpty = true;
                    this.hasMore = false;
                    this.isFullScreen = false;
                });
            },
            // 请求下一页
            loadNest(){
                this.isLoading = true;
                const req ={...params, skip: this.tableData.length, pagesize: this.curPageSize, type: this.type, reportDate:this.selTime};
                this.$get('/finchinaAPP/nonStandard/getInvestmentPortfolioDetail.action', req).then(res => {
                    this.tableData.push(...res.data.list);
                    this.isLoading = false;
                    this.hasMore = res.data.hasMore === 1;
                    // 判断加载完是否到底部
                    this.ifFullScreen();
                }).catch(err=>{
                    this.hasMore = false;
                    this.isLoading = false;
                    // 判断加载完是否到底部
                    this.ifFullScreen();
                });
            },
            autoGet(){
                this.$nextTick(() => {
                    let t = document.querySelector("." + `tbody${this.type}`);
                    //滚动条滚到底部时：窗口高度+滚动条高度 = 内容区高度
                    let scrollTop = t.scrollTop,//滚动条高度
                        wHeight = t.clientHeight,//窗口高度
                        conHeight = t.scrollHeight;//内容高度

                    //在还有200px滚到底部之前加载下一页
                    let isScrollBottom = conHeight - wHeight - scrollTop <= this.bottomHeight;
                    if (this.hasMore && isScrollBottom && !this.isLoading) {
                        this.loadNest();
                    }
                })
            },
            //内容是否铺满屏幕
            ifFullScreen(){
                // 若内容未到底部，判断加载完是否到底部
                if (!this.maskVisible && !this.isFullScreen && !this.isEmpty){
                    this.$nextTick(() => {
                        //窗口高度
                        let wHeight = document.querySelector("#" + `mainContent${this.type}`).clientHeight;
                        if (wHeight > 0){
                            //内容高度
                            let conHeight = document.querySelector("." + `tbody${this.type}`).scrollHeight;
                            this.isFullScreen = wHeight - conHeight <= 150;
                        }
                    })
                }
            }
        },
        components: {
            noFound, bottomStatus
        },
        directives: {
            scrollFix
        },
    }
</script>

<style lang="less">
    @import "../../../assets/less/base";

    #app {
        .menu-cover {
            position: fixed;
            width: 100%;
            height: 100%;
            z-index: 4;
            top: 0;
            background: rgba(17, 17, 17, 0.3);
        }
        .content{
            height: calc(100% - 0.63rem);
        }
        .main-box{
            height: 100%;
            .end-time{
                padding-top: 0.24rem;
                padding-bottom: 0.32rem;
                font-family: PingFangSC-Regular;
                font-size: 0.24rem;
                text-align: right;
                color: #FF7500;
                line-height: 0.24rem;
                top:0;
                z-index: 5;
                width: 100%;
                .time-option {
                    font-family: PingFangSC-Regular;
                    font-size: 0.24rem;
                    margin-right: 0.33rem;
                    position: relative;
                    padding-right: 0.25rem;
                    &:after {
                        content: '';
                        display: block;
                        position: absolute;
                        border: 0.1rem solid transparent;
                        margin-top: -0.05rem;
                        border-top-color: #ff7500;
                        right: 0;
                        top: 50%;
                        transition: all 0.3s;
                        border-radius: 0.05rem;
                    }
                    &.menu-active {
                        color: #ff7500;
                        &:after {
                            margin-top: -0.05rem;
                            transform-origin: center 0.04rem;
                            transform: rotate(180deg);
                            border-top-color: #ff7500;
                        }
                    }
                }
            }
            .menu-cover {
                width: 1.68rem;
                background: #fff;
                padding: 0.24rem 0;
                position: absolute;
                z-index: 9999999;
                right: 0.13rem;
                border-radius: 0.04rem;
                height: auto;
                max-height: 5.6rem;
                top: 1.15rem;
                &:before{
                    content: '';
                    display: inline-block;
                    vertical-align: top;
                    border: 0.13rem solid transparent;
                    border-bottom-color: #fff;
                    position: absolute;
                    top: -0.25rem;
                    right: 0.16rem;
                }
                ul{
                    height: auto;
                    max-height: 5.6rem;
                    overflow-y: scroll;
                    -webkit-overflow-scrolling: touch;
                }
                li {
                    font-family: PingFangSC-Regular;
                    font-size: 0.26rem;
                    color: #111111;
                    text-align: center;
                    line-height: 0.32rem;
                    &.menu-selected {
                        color: #FF7500;
                    }
                    &:not(:last-child){
                        margin-bottom: 0.32rem;
                    }
                }
            }
            .main{
                height: calc(100% - 0.8rem);
            }
        }
        table{
            width: 100%;
            height: 100%;
            text-align: right;
            tr{
                width: calc(100% - 0.56rem);
                margin: 0 0.28rem;
            }
            th{
                font-family: PingFangSC-Medium;
                font-size: 0.24rem;
                color: #333333;
                letter-spacing: 0;
                line-height: 0.24rem;

                padding: 0 0 0.28rem 0;
                &:first-child{
                    text-align: left;
                }
                &:nth-child(2){
                    text-align: left;
                }
            }
            td{
                font-family: PingFangSC-Regular;
                font-size: 0.26rem;
                letter-spacing: 0;
                line-height: 0.26rem;
                color:  #111111;
                padding-bottom: 0.30rem;
                &:first-child{
                    text-align: left;
                }
                &:nth-child(2){
                    text-align: left;
                }
            }
        }
        .red-start{
            color: red;
        }
        .bottom-msg {
            padding-top: 0.48rem;
        }
    }
    .indicator{
    .indicator-mask {
        top: 0.63rem;
        height: calc(100% - 0.63rem);
    }
    }
</style>