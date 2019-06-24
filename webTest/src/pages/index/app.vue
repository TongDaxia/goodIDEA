<template>
    <div id="app">
        <div v-show="showTimeDown" class="menu-cover" @touchmove.prevent @click="showTimeDown=false"></div>
            <!--固定头部-->
            <div id="fixedHead" class="fixed-head">
                <!--头部数字-->
                <head-number ref="headNumber"></head-number>
                <!--头部导航-->
                <div class="tab">
                    <span v-for="(v,i) in tabOption"
                          :class="{'cur':i === activeTab}"
                          @click="changeTab(i)">{{v.v}}</span>
                </div>
            </div>
            <!--页面主体内容-->
            <div class="main-box">
                <!--时间下拉选项-->
                <ul v-show="showTimeDown" class="time-menu" @touchmove.prevent>
                    <li v-for="d in timeOption"
                        @click="selecteTime(d)"
                        :class="{'menu-selected':d.v === selTime.v}">{{d.k}}
                    </li>
                </ul>
                <div id="mainContent" class="main-content">
                    <template v-for="(k,i) in tabOption">
                        <component :is="k.k" :ref="`panel${i}`" v-if="i === 0"
                                   :selTime="selTime"
                                   :showTimeDown="showTimeDown"
                                   :isCurrencyType="isCurrencyType"
                                   @switchTimeDown="switchTimeDown"></component>
                        <component :is="k.k" :ref="`panel${i}`" v-else></component>
                    </template>
                </div>
            </div>
    </div>
</template>

<script>
    import echarts from 'echarts/dist/echarts.common';
    import { throttle } from 'lodash';
    import headNumber from './components/headNumber';
    import netValue from "./components/netValue";
    import notice from './components/notice';
    import product from './components/product';
    import manager from './components/manager';
    import shareBonus from './components/separateBonus';
    import investPortfolio from './components/investPortfolio';
    import shareChange from './components/separateChange';
    import heavyStock from './components/heavyStock';
    import relatedSecurities from './components/relatedSecurities';
    import common from '@utils/commons';
    export default {
        data() {
            return {
                isCurrencyType:1,
                showTimeDown: false,//是否显示时间下拉
                selTime:{k:'近1年',v:3},//选中的时间项
                timeOption: common.confOption().time,
                tabOption:common.confOption().tab,
                activeTab: 0,//当前活动的tab项
            }
        },
        created(){},
        mounted() {
            common.resize();//设置根字体rem
            common.bindScrollEvt();//监听滚动事件通知APP
            this.$nextTick(()=>{
                this.autoFillFoot();
            });
            this.initScroll();
            this.init();
        },
        methods: {
            /**
             * 页面初始化
             */
             init(){
                this.$loading.open();
                this.$refs['headNumber'].loadData().then(res=>{
                    if(res){
                        //1是普通型，2是货币型
                        const {type,isCurrencyType,establishDateParam,managerCode} = res;
                        this.isCurrencyType = isCurrencyType;
                        const localReq = {type,isCurrencyType,establishDateParam,managerCode};
                        localStorage.setItem("localReq",JSON.stringify(localReq));
                    }else{
                        this.$loading.close();
                    }
                    this.getFirstPannel();
                });
            },
            //首先加第一个pannel，然后再加载后续所有pannnel
            getFirstPannel(){//TODO
                const that = this;
                const chart = that.$refs['panel0'][0].getChartData(),
                    table = that.$refs['panel0'][0].getNetValueTable(),
                    quota = that.$refs['panel0'][0].getMainQuota(),
                    income = that.$refs['panel0'][0].getIntervalIncome();
                Promise.all([chart,table,quota,income]).then(() => {
                    this.$loading.close();
                    for (let name in that.$refs){
                        if(name !=='headNumber'){
                            that.$refs[name][0].loadData();
                        }
                    }
                })
            },
            //自动撑开底部
            autoFillFoot(){
                const headHeight = document.getElementById('fixedHead').offsetHeight;//头部固定高度
                let pannel = document.getElementsByClassName('pannel');//滚动块
                const windowHeight = common.getWindowHeight(),
                    lastPanelHeight = pannel[pannel.length - 1].clientHeight;

                let needPatchBottom = false;
                if (navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)) {
                    if ((screen.height === 812 && screen.width === 375 && window.devicePixelRatio === 3) ||
                        ((screen.height === 896 && screen.width === 414) && (window.devicePixelRatio === 2 || window.devicePixelRatio === 3 ))){
                        needPatchBottom = true;
                    }
                }

                if (lastPanelHeight < windowHeight) {
                    //getComputedStyle(document.querySelector('.related-securities'))['padding-bottom']
                    const paddingBottom = parseFloat(getComputedStyle(pannel[pannel.length-1])['padding-bottom']);
                    pannel[pannel.length - 1].style.height = `${windowHeight - headHeight - paddingBottom + (needPatchBottom ? 34 : 0)}px`;
                }
            },
            //监听页面滚动事件
            initScroll(){
                const that = this;
                let content = document.getElementById('mainContent'),//滚动盒
                    pannel = document.querySelectorAll('.pannel');//滚动块
                content.addEventListener('scroll', throttle(function scrolls() {
                    const content = document.querySelector('#mainContent');
                    for (let i = that.tabOption.length; i > 0; i--) {
                        //scrollTop加1是为了解决scrollTop值不够精确的问题，不能够精确到小数位
                        //js会对其自动四舍五入，导致计算出现偏差
                        if (content.scrollTop + 1 >= pannel[i - 1].offsetTop) {
                            that.activeTab = i-1;
                            break;
                        }
                    }
                }, 0));
            },
            //切换当前活动的tab，并置顶
            changeTab(i) {
                document.querySelector('#mainContent').scrollTop = document.querySelectorAll('.pannel')[i].offsetTop;
            },
            //切换时间选项值
            selecteTime(d) {
                this.selTime = d;
                this.showTimeDown = false;
                this.$nextTick(()=>{
                    this.$refs['panel0'][0].getChartData();
                })

            },
            switchTimeDown(){
                this.showTimeDown = !this.showTimeDown;
            }
        },
        watch: {
            activeTab(v){
                let $tab = $('#fixedHead .tab'),
                    tabWidth = $tab[0].offsetWidth,
                    tabPaddingLeft = parseFloat(getComputedStyle($tab[0])['paddingLeft']),
                    scrollLeft = $tab[0].scrollLeft;

                let node = $tab.find('span')[v],
                    nodeOffsetLeft = node.offsetLeft,
                    marginLeft = parseFloat(getComputedStyle(node)['marginLeft']),
                    spanPlaceWidth = node.offsetWidth + marginLeft;

                if(scrollLeft > nodeOffsetLeft) {
                    $tab.scrollLeft(nodeOffsetLeft - marginLeft - tabPaddingLeft);
                }else if(scrollLeft + tabWidth < nodeOffsetLeft + spanPlaceWidth) {
                    $tab.scrollLeft(nodeOffsetLeft - tabWidth + spanPlaceWidth + tabPaddingLeft);
                }
            },
        },
        components: {headNumber, netValue, notice, product, manager, shareBonus,
            investPortfolio, shareChange, heavyStock, relatedSecurities},
    }
</script>

<style lang="less">
    @import "../../assets/less/base";
    .menu-cover {
        position: fixed;
        width: 100%;
        height: 100%;
        z-index: 4;
        top: 0;
        background: rgba(17, 17, 17, 0.3);
    }
    #app{
        background: #F6F6F6;
    }
    .fixed-head {
        .tab {
            white-space: nowrap;
            border-bottom: 0.02rem solid #E6E6E6;
            padding: 0.18rem 0.12rem 0.23rem 0.12rem;
            margin-top: 0.16rem;
            background: #fff;
            z-index: 3;
            overflow-x: scroll;
            position: relative;
            -webkit-overflow-scrolling: touch;
            span {
                font-size: 0.32rem;
                vertical-align: top;
                margin: 0 0.2rem;
                color: #333;
                line-height: 0.34rem;
                display: inline-block;
                &.cur {
                    color: #1482F0;
                    font-weight: 600;
                    position: relative;
                    &:before{
                        content: "";
                        position: absolute;
                        left: 50%;
                        top: 0.42rem;
                        margin-left: -0.15rem;
                        width: 0.32rem;
                        height: 0.04rem;
                        -webkit-border-radius: 3px;
                        border-radius: 5px;
                        background: #1482F0;
                    }
                }
            }
        }
    }
    .main-box{
        height: calc(100% - 2.43rem);
        position: relative;
        .time-menu{
            width: 1.68rem;
            background: #fff;
            padding: 0.24rem 0;
            position: absolute;
            z-index: 99999999;
            right: 0.15rem;
            border-radius: 0.04rem;
            top:0.64rem;
            &:before{
                content: '';
                display: inline-block;
                vertical-align: top;
                border: 0.13rem solid transparent;
                border-bottom-color: #fff;
                position: absolute;
                top: -0.25rem;
                right: 0.15rem;
            }
            li {
                font-size: 0.26rem;
                text-align: center;
                line-height: 0.32rem;
                &.menu-selected {
                    color: #ff6b12;
                }
                &:not(:last-child){
                    margin-bottom: 0.32rem;
                }
            }
        }
    }
    .main-content {
        overflow: auto;
        height: 100%;
        /*webkit-overflow-scrolling会导致苹果上z-index属性失效*/
        -webkit-overflow-scrolling: touch;
        .pannel{
            background: #fff;
            margin-top: 0.16rem;
            padding-bottom: 0.24rem;
        }
    }
    /*loading样式*/
    .indicator {
        .indicator-mask{
            /*出现遮罩时，会导致div无法滚动*/
            display: none;
            background: transparent;
        }

        .indicator-wrapper{
            background: transparent;
        }
    }
    /*pannel标题样式*/
    .head-title {
        padding: 0.32rem 0.32rem 0 0.32rem;
        position: relative;
        a{
            display: inline-block;
            height: 100%;
            width: 100%;
            position: absolute;
            top: 0;
            left: 0;
            -webkit-tap-highlight-color: rgba(255, 255, 255, 0);
            -webkit-user-select: none;
            -moz-user-focus: none;
            -moz-user-select: none;
        }
        .title-box{
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .title{
            color: #3c3c3c;
            font-size: 0.32rem;
            line-height: 0.32rem;
            font-weight: 600;
            display: inline-block;
            &:before{
                content: '';
                width: 0.08rem;
                height: 0.32rem;
                background: #1E9DF8;
                display: inline-block;
                vertical-align: top;
                margin-right: 0.16rem;
            }
        }
        .btn{
            display: inline-block;
            width: 0.135rem;
            height: 0.135rem;
            border: 0.03rem solid;
            border-color: transparent #8C8C8C #8C8C8C transparent;
            transform: rotate(-45deg);
        }
    }
    /*表格样式*/
    .detail-table {
        padding: 0 0.32rem 0 0.32rem;
        width: calc(100% - 0.64rem);
        margin: 0 auto;
        border-collapse: collapse;
        border-spacing: 0;
        th {
            font-weight: 600;
            font-size: 0.24rem;
            color: #333;
            text-align: right;
            padding-top: 0.36rem;
            white-space: nowrap;
        }
        td {
            font-size: 0.26rem;
            color: #111;
            padding-top: 0.3rem;
            text-align: right;
            line-height: 0.26rem;
        }
        td:first-child,
        th:first-child {
            text-align: left;
        }
    }
</style>
<style>
    @media only screen and (device-width: 375px) and (device-height: 812px) and (-webkit-device-pixel-ratio: 3),
    only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 2),
    only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 3) {
        :root {
            height: calc(100% - 1px + constant(safe-area-inset-bottom) + 1px);
            height: calc(100% - 1px + env(safe-area-inset-bottom) + 1px)
        }
    }
</style>
