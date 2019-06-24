<template>
    <!--相关证券相同发行人详情页-->
    <div id="app">
        <!--头部导航-->
        <div class="head">
            <span v-for="(v, i) in tabs"
                  :class="{'cur': i === activeTab}"
                  @click="changeTab(i)">{{v}}</span>
        </div>
        <template v-for="(v,i) in tabs">
            <component is="tableDetail" :type="i" :ref="`tab${i}`" v-show="activeTab === i"></component>
        </template>
    </div>
</template>

<script>
    import tableDetail from './components/tableDetail';
    import common from '@utils/commons';
    export default {
        data() {
            return {
                tabs:['股票', '混合型', '债券型', '货币型', '其他'],
                activeTab: 0 //当前活动的tab项
            }
        },
        mounted() {
            common.resize();
            common.bindScrollEvt();
            this.$nextTick(() => {
                common.addWaterMark();
            });
        },
        methods: {
            changeTab: function (v) {
                this.activeTab = v;
                this.$refs['tab' + v][0].ifFullScreen();
            }
        },
        components: {
            tableDetail
        },
    }
</script>

<style lang="less">
    @import "../../assets/less/base";
    #app {
        .head{
            font-family: PingFangSC-Regular;
            font-size: 0.24rem;
            width: 100%;
            height: 0.62rem;
            line-height: 0.62rem;
            text-align: center;
            border-bottom: 0.01rem solid #DFDFDF;
            display: flex;
            align-items: center;
            span{
                width: 100%;
                flex: 1;
                line-height: 0.28rem;
                color: #8C8C8C;
                border-right: 0.01rem solid #979797;
                &:last-child{
                    border-right-color: transparent;
                }
                &.cur {
                    color: #1482F0;
                }
            }
        }
    }
</style>
