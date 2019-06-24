<template>
    <div id="app">
        <!-- 分红面板 -->
        <share-detail ref="sharePanel"></share-detail>
        <!-- 拆分面板 -->
        <split-detail ref="splitPanel"></split-detail>
    </div>
</template>

<script>
    import common from '@utils/commons';
    import shareDetail from './components/shareDetail';
    import splitDetail from './components/splitDetail';

    export default {
        mounted() {
            common.resize();
            common.bindScrollEvt();
            this.$nextTick(() => {
                common.addWaterMark();
            });
            this.init()
        },
        methods: {
            init(){
                this.$loading.open();
                const share = this.$refs['sharePanel'].loadData(),
                    split = this.$refs['splitPanel'].loadData();
                Promise.all([share,split]).then(() => {
                    this.$loading.close();
                })
            }
        },
        components: {
            shareDetail,
            splitDetail
        },
    }
</script>

<style lang="less">
    @import "../../assets/less/base";
    #app {
        height: 100%;
        background-color: #F6F6F6;
        .title-box{
            padding: 0.32rem;
            position: relative;
            display: flex;
            align-items: center;
            justify-content: space-between;
            .title{
                font-family: PingFangSC-Medium;
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
        }
        th{
            font-family: PingFangSC-Medium;
            font-size: 0.26rem;
            color: #333333;
            padding-bottom: 0.3rem;
        }
        td{
            font-family: PingFangSC-Regular;
            font-size: 0.26rem;
            color: #111111;
            padding-bottom: 0.3rem;
        }
        /*无数据样式*/
        .no-data{
            font-size: 0.24rem;
            color: #8c8c8c;
            display: block;
            text-align: center;
            padding: 0.1rem 0 0.48rem 0;
        }
        //分页样式
        .pagination{
            text-align: right;
            padding: 0.2rem 0.32rem 0.24rem 0.32rem;
            span{
                font-family: PingFangSC-Regular;
                font-size: 0.24rem;
                margin-left: 0.2rem;
                color: #1482f0;
                &.num{
                    font-family: Helvetica;
                    font-size: 0.24rem;
                    color: #3C3C3C;
                }
                &.disabled{
                    color: #8c8c8c;
                }
            }
        }
    }
</style>
