import ModalVue from './modal';
import Vue from 'vue';

const defaults = {
  title: null,
  message: '',
  beforeClose: null,
  confirmButtonText: '我知道了',
  cancelButtonText: '',
  confirmButtonClass: '',
  cancelButtonClass: '',
  showConfirmButton: true,
  showCancelButton: false,
  buttonType: 'button',
  confirmLink: '',
  cancelLink: '',
  onClose: null,
};

let msgQueue = [],
  instance,
  currentMsg;

const defaultCallback = action => {
  if (currentMsg) {
    let callback = currentMsg.callback;
    if (typeof callback === 'function') {
      callback(action);
    }
    if (currentMsg.resolve) {
      if (action === 'confirm') {
        currentMsg.resolve(action);
      } else if (currentMsg.reject && action === 'cancel') {
        currentMsg.reject(action);
      }
    }
  }
};

const merge = Object.assign;

const modalConstructor = Vue.extend(ModalVue);

const initInstance = () => {
  instance = new modalConstructor({
    el: document.createElement('div'),
  });
};

const showNextMsg = () => {
  if (!instance) {
    initInstance();
  }

  if (!instance.visible) {
    if (msgQueue.length > 0) {
      currentMsg = msgQueue.shift();

      const options = currentMsg.options;
      for (let prop in options) {
        if (options.hasOwnProperty(prop)) {
          instance[prop] = options[prop];
        }
      }
      if (options.callback === undefined) {
        instance.callback = defaultCallback;
      }

      let oldCb = instance.callback;
      instance.callback = (action, instance) => {
        oldCb(action, instance);
        showNextMsg();
      };
      document.body.appendChild(instance.$el);

      Vue.nextTick(() => {
        instance.visible = true;
      });
    }
  }
};

class MsgBox {
  constructor(options, callback) {
    if (Vue.prototype.$isServer) {
      return;
    }
    if (typeof options === 'string') {
      options = {
        message: options,
      };
      if (typeof arguments[1] === 'string') {
        options.title = arguments[1];
      }
    } else if (options.callback && !callback) {
      callback = options.callback;
    }

    if (typeof Promise !== 'undefined') {
      return new Promise((resolve, reject) => {
        msgQueue.push({
          options: Object.assign({}, defaults, MsgBox.defaults, options),
          callback,
          resolve,
          reject,
        });

        showNextMsg();
      });
    }
  }

  static setDefaults = (defaults = {}) => {
    MsgBox.defaults = defaults;
  };

  static alert(message, title, options) {
    if (typeof message === 'object') {
      options = message;
      message = options.message || '';
    } else if (title === undefined) {
      title = '';
    }

    return new MsgBox(
      merge(
        {
          title,
          message,
        },
        options
      )
    );
  }

  static close() {
    instance.doClose();
    instance.visible = false;
    msgQueue = [];
    currentMsg = null;
  }
}

export class Modal {
  static install(Vue) {
    Vue.prototype.$msgbox = MsgBox;
    Vue.prototype.$alert = MsgBox.alert;
  }
}

export default Modal;
