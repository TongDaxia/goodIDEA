<template>
  <div class="power-hint" v-if="visible">
    <div class="hint-content" v-scroll-fix>
      <div class="up-content">
        <h3>{{ title }}</h3>
        <div>{{ message }}</div>
      </div>
      <div class="btn-grounp">
        <custom-button
          :type="buttonType"
          :link="getLink('cancel')"
          @click="handleAction('cancel', $event)"
          v-if="showCancelButton"
          :class="cancelButtonClass"
        >
          {{ cancelButtonText }}
        </custom-button>
        <custom-button
          :type="buttonType"
          :link="getLink('confirm')"
          @click="handleAction('confirm', $event)"
          v-if="showConfirmButton"
          :class="confirmButtonClass"
        >
          {{ confirmButtonText }}
        </custom-button>
      </div>
    </div>
    <div class="hint-mask" v-scroll-fix></div>
  </div>
</template>

<script>
import scrollFix from '../director/scrollFix';
export default {
  props: {
    title: String,
    message: String,
    cancelButtonText: String,
    confirmButtonText: String,
    showConfirmButton: Boolean,
    showCancelButton: Boolean,
    confirmButtonClass: [Array, Object, String],
    cancelButtonClass: [Array, Object, String],
    confirmLink: String,
    cancelLink: String,
    visible: {
      type: Boolean,
      default: false,
    },
    buttonType: {
      validator(v) {
        return ['button', 'link'].indexOf(v) > -1;
      },
      default: 'button',
    },
  },
  directives: { scrollFix },
  components: {
    CustomButton: {
      props: {
        type: String,
        link: String,
      },
      computed: {
        eventListeners() {
          return Object.assign({}, this.$listeners);
        },
      },
      render(h) {
        const isLink = this.type === 'link';
        const attrs = isLink ? { target: '_blank', href: this.link } : {};
        return h(
          !isLink ? 'button' : 'a',
          {
            on: this.eventListeners,
            attrs,
          },
          this.$slots.default
        );
      },
    },
  },
  methods: {
    getLink(action) {
      switch (action) {
        case 'confirm':
          return this.confirmLink;
        case 'cancel':
          return this.cancelLink;
        default:
          return '';
      }
    },
    getSafeClose() {
      const currentId = this._uid;
      return () => {
        this.$nextTick(() => {
          if (currentId === this._uid) {
            this.doClose();
          }
        });
      };
    },
    doClose() {
      if (!this.visible) return;
      this.visible = false;

      this.onClose && this.onClose();
      setTimeout(() => {
        if (this.action) this.callback(this.action, this);
      });
    },
    handleAction(action, event) {
      this.action = action;
      if (typeof this.beforeClose === 'function') {
        this.close = this.getSafeClose();
        this.beforeClose(action, this, this.close, event);
      } else {
        this.doClose();
      }
    },
  },
};
</script>

<style lang="less" scoped>
.power-hint {
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  z-index: 999999;

  .hint-mask {
    position: fixed;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    margin: 0 auto;
    z-index: 11;
    background-color: rgba(140, 140, 140, 0.5);
  }

  .hint-content {
    width: 5.5rem;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    z-index: 12;
    background: #fff;
    border-radius: 0.32rem;

    .up-content {
      margin: 0.22rem 0 0;
      border-bottom: 0.01rem solid #d4d5d7;

      h3 {
        width: 100%;
        margin: 0.2rem 0;
        text-align: center;
        font-size: 0.34rem;
        line-height: 1;
      }

      div {
        font-size: 0.28rem;
        line-height: 0.38rem;
        padding: 0 0.45rem 0.34rem;
      }
    }

    .btn-grounp {
      display: flex;

      button,
      a {
        height: 0.88rem;
        line-height: 0.88rem;
        flex: 1;
        text-align: center;
        display: inline-block;
        color: #007aff;
        font-size: 0.34rem;
        background-color: transparent;
        border: none;
        cursor: pointer;
        &:nth-of-type(2) {
          border-left: 0.015rem solid #d4d5d7;
        }
      }
    }
  }
}
</style>
