
var runtime = (function (exports) {

  var Op = Object.prototype;
  var hasOwn = Op.hasOwnProperty;
  var undefined;
  var $Symbol = typeof Symbol === "function" ? Symbol : {};
  var iteratorSymbol = $Symbol.iterator || "@@iterator";
  var asyncIteratorSymbol = $Symbol.asyncIterator || "@@asyncIterator";
  var toStringTagSymbol = $Symbol.toStringTag || "@@toStringTag";

  function wrap(innerFn, outerFn, self, tryLocsList) {
    var protoGenerator = outerFn && outerFn.prototype instanceof Generator ? outerFn : Generator;
    var generator = Object.create(protoGenerator.prototype);
    var context = new Context(tryLocsList || []);

    generator._invoke = makeInvokeMethod(innerFn, self, context);

    return generator;
  }
  exports.wrap = wrap;

  function tryCatch(fn, obj, arg) {
    try {
      return { type: "normal", arg: fn.call(obj, arg) };
    } catch (err) {
      return { type: "throw", arg: err };
    }
  }

  var GenStateSuspendedStart = "suspendedStart";
  var GenStateSuspendedYield = "suspendedYield";
  var GenStateExecuting = "executing";
  var GenStateCompleted = "completed";

  var ContinueSentinel = {};

  function Generator() {}
  function GeneratorFunction() {}
  function GeneratorFunctionPrototype() {}

  var IteratorPrototype = {};
  IteratorPrototype[iteratorSymbol] = function () {
    return this;
  };

  var getProto = Object.getPrototypeOf;
  var NativeIteratorPrototype = getProto && getProto(getProto(values([])));
  if (NativeIteratorPrototype &&
      NativeIteratorPrototype !== Op &&
      hasOwn.call(NativeIteratorPrototype, iteratorSymbol)) {
    IteratorPrototype = NativeIteratorPrototype;
  }

  var Gp = GeneratorFunctionPrototype.prototype =
    Generator.prototype = Object.create(IteratorPrototype);
  GeneratorFunction.prototype = Gp.constructor = GeneratorFunctionPrototype;
  GeneratorFunctionPrototype.constructor = GeneratorFunction;
  GeneratorFunctionPrototype[toStringTagSymbol] =
    GeneratorFunction.displayName = "GeneratorFunction";

  function defineIteratorMethods(prototype) {
    ["next", "throw", "return"].forEach(function(method) {
      prototype[method] = function(arg) {
        return this._invoke(method, arg);
      };
    });
  }

  exports.isGeneratorFunction = function(genFun) {
    var ctor = typeof genFun === "function" && genFun.constructor;
    return ctor
      ? ctor === GeneratorFunction ||
        (ctor.displayName || ctor.name) === "GeneratorFunction"
      : false;
  };

  exports.mark = function(genFun) {
    if (Object.setPrototypeOf) {
      Object.setPrototypeOf(genFun, GeneratorFunctionPrototype);
    } else {
      genFun.__proto__ = GeneratorFunctionPrototype;
      if (!(toStringTagSymbol in genFun)) {
        genFun[toStringTagSymbol] = "GeneratorFunction";
      }
    }
    genFun.prototype = Object.create(Gp);
    return genFun;
  };

  exports.awrap = function(arg) {
    return { __await: arg };
  };

  function AsyncIterator(generator) {
    function invoke(method, arg, resolve, reject) {
      var record = tryCatch(generator[method], generator, arg);
      if (record.type === "throw") {
        reject(record.arg);
      } else {
        var result = record.arg;
        var value = result.value;
        if (value &&
            typeof value === "object" &&
            hasOwn.call(value, "__await")) {
          return Promise.resolve(value.__await).then(function(value) {
            invoke("next", value, resolve, reject);
          }, function(err) {
            invoke("throw", err, resolve, reject);
          });
        }

        return Promise.resolve(value).then(function(unwrapped) {
          result.value = unwrapped;
          resolve(result);
        }, function(error) {
          return invoke("throw", error, resolve, reject);
        });
      }
    }

    var previousPromise;

    function enqueue(method, arg) {
      function callInvokeWithMethodAndArg() {
        return new Promise(function(resolve, reject) {
          invoke(method, arg, resolve, reject);
        });
      }

      return previousPromise =
        previousPromise ? previousPromise.then(
          callInvokeWithMethodAndArg,
          callInvokeWithMethodAndArg
        ) : callInvokeWithMethodAndArg();
    }

    this._invoke = enqueue;
  }

  defineIteratorMethods(AsyncIterator.prototype);
  AsyncIterator.prototype[asyncIteratorSymbol] = function () {
    return this;
  };
  exports.AsyncIterator = AsyncIterator;

  exports.async = function(innerFn, outerFn, self, tryLocsList) {
    var iter = new AsyncIterator(
      wrap(innerFn, outerFn, self, tryLocsList)
    );

    return exports.isGeneratorFunction(outerFn)
      ? iter
      : iter.next().then(function(result) {
          return result.done ? result.value : iter.next();
        });
  };

  function makeInvokeMethod(innerFn, self, context) {
    var state = GenStateSuspendedStart;

    return function invoke(method, arg) {
      if (state === GenStateExecuting) {
        throw new Error("Generator is already running");
      }

      if (state === GenStateCompleted) {
        if (method === "throw") {
          throw arg;
        }

        return doneResult();
      }

      context.method = method;
      context.arg = arg;

      while (true) {
        var delegate = context.delegate;
        if (delegate) {
          var delegateResult = maybeInvokeDelegate(delegate, context);
          if (delegateResult) {
            if (delegateResult === ContinueSentinel) continue;
            return delegateResult;
          }
        }

        if (context.method === "next") {
          context.sent = context._sent = context.arg;

        } else if (context.method === "throw") {
          if (state === GenStateSuspendedStart) {
            state = GenStateCompleted;
            throw context.arg;
          }

          context.dispatchException(context.arg);

        } else if (context.method === "return") {
          context.abrupt("return", context.arg);
        }

        state = GenStateExecuting;

        var record = tryCatch(innerFn, self, context);
        if (record.type === "normal") {
          state = context.done
            ? GenStateCompleted
            : GenStateSuspendedYield;

          if (record.arg === ContinueSentinel) {
            continue;
          }

          return {
            value: record.arg,
            done: context.done
          };

        } else if (record.type === "throw") {
          state = GenStateCompleted;
          context.method = "throw";
          context.arg = record.arg;
        }
      }
    };
  }

  function maybeInvokeDelegate(delegate, context) {
    var method = delegate.iterator[context.method];
    if (method === undefined) {
      context.delegate = null;

      if (context.method === "throw") {
        if (delegate.iterator["return"]) {
          context.method = "return";
          context.arg = undefined;
          maybeInvokeDelegate(delegate, context);

          if (context.method === "throw") {
            return ContinueSentinel;
          }
        }

        context.method = "throw";
        context.arg = new TypeError(
          "The iterator does not provide a 'throw' method");
      }

      return ContinueSentinel;
    }

    var record = tryCatch(method, delegate.iterator, context.arg);

    if (record.type === "throw") {
      context.method = "throw";
      context.arg = record.arg;
      context.delegate = null;
      return ContinueSentinel;
    }

    var info = record.arg;

    if (! info) {
      context.method = "throw";
      context.arg = new TypeError("iterator result is not an object");
      context.delegate = null;
      return ContinueSentinel;
    }

    if (info.done) {
      context[delegate.resultName] = info.value;

      context.next = delegate.nextLoc;

      if (context.method !== "return") {
        context.method = "next";
        context.arg = undefined;
      }

    } else {
      return info;
    }

    context.delegate = null;
    return ContinueSentinel;
  }

  defineIteratorMethods(Gp);

  Gp[toStringTagSymbol] = "Generator";

  Gp[iteratorSymbol] = function() {
    return this;
  };

  Gp.toString = function() {
    return "[object Generator]";
  };

  function pushTryEntry(locs) {
    var entry = { tryLoc: locs[0] };

    if (1 in locs) {
      entry.catchLoc = locs[1];
    }

    if (2 in locs) {
      entry.finallyLoc = locs[2];
      entry.afterLoc = locs[3];
    }

    this.tryEntries.push(entry);
  }

  function resetTryEntry(entry) {
    var record = entry.completion || {};
    record.type = "normal";
    delete record.arg;
    entry.completion = record;
  }

  function Context(tryLocsList) {
    this.tryEntries = [{ tryLoc: "root" }];
    tryLocsList.forEach(pushTryEntry, this);
    this.reset(true);
  }

  exports.keys = function(object) {
    var keys = [];
    for (var key in object) {
      keys.push(key);
    }
    keys.reverse();

    return function next() {
      while (keys.length) {
        var key = keys.pop();
        if (key in object) {
          next.value = key;
          next.done = false;
          return next;
        }
      }

      next.done = true;
      return next;
    };
  };

  function values(iterable) {
    if (iterable) {
      var iteratorMethod = iterable[iteratorSymbol];
      if (iteratorMethod) {
        return iteratorMethod.call(iterable);
      }

      if (typeof iterable.next === "function") {
        return iterable;
      }

      if (!isNaN(iterable.length)) {
        var i = -1, next = function next() {
          while (++i < iterable.length) {
            if (hasOwn.call(iterable, i)) {
              next.value = iterable[i];
              next.done = false;
              return next;
            }
          }

          next.value = undefined;
          next.done = true;

          return next;
        };

        return next.next = next;
      }
    }

    return { next: doneResult };
  }
  exports.values = values;

  function doneResult() {
    return { value: undefined, done: true };
  }

  Context.prototype = {
    constructor: Context,

    reset: function(skipTempReset) {
      this.prev = 0;
      this.next = 0;
      this.sent = this._sent = undefined;
      this.done = false;
      this.delegate = null;

      this.method = "next";
      this.arg = undefined;

      this.tryEntries.forEach(resetTryEntry);

      if (!skipTempReset) {
        for (var name in this) {
          if (name.charAt(0) === "t" &&
              hasOwn.call(this, name) &&
              !isNaN(+name.slice(1))) {
            this[name] = undefined;
          }
        }
      }
    },

    stop: function() {
      this.done = true;

      var rootEntry = this.tryEntries[0];
      var rootRecord = rootEntry.completion;
      if (rootRecord.type === "throw") {
        throw rootRecord.arg;
      }

      return this.rval;
    },

    dispatchException: function(exception) {
      if (this.done) {
        throw exception;
      }

      var context = this;
      function handle(loc, caught) {
        record.type = "throw";
        record.arg = exception;
        context.next = loc;

        if (caught) {
          context.method = "next";
          context.arg = undefined;
        }

        return !! caught;
      }

      for (var i = this.tryEntries.length - 1; i >= 0; --i) {
        var entry = this.tryEntries[i];
        var record = entry.completion;

        if (entry.tryLoc === "root") {
          return handle("end");
        }

        if (entry.tryLoc <= this.prev) {
          var hasCatch = hasOwn.call(entry, "catchLoc");
          var hasFinally = hasOwn.call(entry, "finallyLoc");

          if (hasCatch && hasFinally) {
            if (this.prev < entry.catchLoc) {
              return handle(entry.catchLoc, true);
            } else if (this.prev < entry.finallyLoc) {
              return handle(entry.finallyLoc);
            }

          } else if (hasCatch) {
            if (this.prev < entry.catchLoc) {
              return handle(entry.catchLoc, true);
            }

          } else if (hasFinally) {
            if (this.prev < entry.finallyLoc) {
              return handle(entry.finallyLoc);
            }

          } else {
            throw new Error("try statement without catch or finally");
          }
        }
      }
    },

    abrupt: function(type, arg) {
      for (var i = this.tryEntries.length - 1; i >= 0; --i) {
        var entry = this.tryEntries[i];
        if (entry.tryLoc <= this.prev &&
            hasOwn.call(entry, "finallyLoc") &&
            this.prev < entry.finallyLoc) {
          var finallyEntry = entry;
          break;
        }
      }

      if (finallyEntry &&
          (type === "break" ||
           type === "continue") &&
          finallyEntry.tryLoc <= arg &&
          arg <= finallyEntry.finallyLoc) {
        finallyEntry = null;
      }

      var record = finallyEntry ? finallyEntry.completion : {};
      record.type = type;
      record.arg = arg;

      if (finallyEntry) {
        this.method = "next";
        this.next = finallyEntry.finallyLoc;
        return ContinueSentinel;
      }

      return this.complete(record);
    },

    complete: function(record, afterLoc) {
      if (record.type === "throw") {
        throw record.arg;
      }

      if (record.type === "break" ||
          record.type === "continue") {
        this.next = record.arg;
      } else if (record.type === "return") {
        this.rval = this.arg = record.arg;
        this.method = "return";
        this.next = "end";
      } else if (record.type === "normal" && afterLoc) {
        this.next = afterLoc;
      }

      return ContinueSentinel;
    },

    finish: function(finallyLoc) {
      for (var i = this.tryEntries.length - 1; i >= 0; --i) {
        var entry = this.tryEntries[i];
        if (entry.finallyLoc === finallyLoc) {
          this.complete(entry.completion, entry.afterLoc);
          resetTryEntry(entry);
          return ContinueSentinel;
        }
      }
    },

    "catch": function(tryLoc) {
      for (var i = this.tryEntries.length - 1; i >= 0; --i) {
        var entry = this.tryEntries[i];
        if (entry.tryLoc === tryLoc) {
          var record = entry.completion;
          if (record.type === "throw") {
            var thrown = record.arg;
            resetTryEntry(entry);
          }
          return thrown;
        }
      }

      throw new Error("illegal catch attempt");
    },

    delegateYield: function(iterable, resultName, nextLoc) {
      this.delegate = {
        iterator: values(iterable),
        resultName: resultName,
        nextLoc: nextLoc
      };

      if (this.method === "next") {
        this.arg = undefined;
      }

      return ContinueSentinel;
    }
  };

  return exports;

}(
  typeof module === "object" ? module.exports : {}
));

try {
  regeneratorRuntime = runtime;
} catch (accidentalStrictMode) {
  Function("r", "regeneratorRuntime = r")(runtime);
}







function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { Promise.resolve(value).then(_next, _throw); } }

function _asyncToGenerator(fn) { return function () { var self = this, args = arguments; return new Promise(function (resolve, reject) { var gen = fn.apply(self, args); function _next(value) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value); } function _throw(err) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err); } _next(undefined); }); }; }

function _instanceof(left, right) { if (right != null && typeof Symbol !== "undefined" && right[Symbol.hasInstance]) { return right[Symbol.hasInstance](left); } else { return left instanceof right; } }

function _classCallCheck(instance, Constructor) { if (!_instanceof(instance, Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function serialNumberFn(){
  return 'serialNumber' + (new Date().getTime() + parseInt(Math.random() * 100000000000));
};

function sendPeRequest(serialNumber, params, methodName) {
  if (navigator.userAgent == 'UlordUosAndroid') {
    window.DappJsBridge.pushMessage(serialNumber, params, methodName);
  } else if (navigator.userAgent == 'PocketUosIos') {
    window.webkit.messageHandlers.pushMessage.postMessage({
      'params': params,
      'serialNumber': serialNumber,
      'methodName': methodName
    });
  }
};

var pefun =
function () {
  function pefun() {
    _classCallCheck(this, pefun);
  }

  _createClass(pefun, [{
    key: "requestMsgSignature",
    value: function requestMsgSignature(params) {
      var serialNumber = serialNumberFn();

      sendPeRequest(serialNumber, params, 'requestMsgSignature');

      return new Promise(function (resolve, reject) {
        window.callbackResult = function (returnSerialNumber, result) {
          if (returnSerialNumber == serialNumber) {
            resolve(JSON.parse(result));
          }
        };
      });
    }
  }, {
    key: "requestSignature",
    value: function requestSignature(params) {
      var serialNumber = serialNumberFn();
      var Jparams = JSON.stringify(params);

      sendPeRequest(serialNumber, Jparams, 'requestSignature');

      return new Promise(function (resolve, reject) {
        window.callbackResult = function (returnSerialNumber, result, err) {
          console.log('---requestSignature---');
          console.log(JSON.stringify(result));
          console.log(err);
          if (returnSerialNumber == serialNumber) {
            if(err) {
               reject(err);
            } else resolve((result));
          }
        };
      });
    }
  }, {
    key: "getAppInfo",
    value: function getAppInfo() {
      var serialNumber = serialNumberFn();

      sendPeRequest(serialNumber, '', 'getAppInfo');

      return new Promise(function (resolve, reject) {
        window.callbackResult = function (returnSerialNumber, result) {
          if (returnSerialNumber == serialNumber) {
            resolve(JSON.parse(result));
          }
        };
      });
    }
  }, {
    key: "walletLanguage",
    value: function walletLanguage() {
      var serialNumber = serialNumberFn();
      sendPeRequest(serialNumber, '', 'walletLanguage');

      return new Promise(function (resolve, reject) {
        window.callbackResult = function (returnSerialNumber, result) {
          if (returnSerialNumber == serialNumber) {
            resolve(JSON.parse(result));
          }
        };
      });
    }
  }, {
    key: "getUosAccount",
    value: function getUosAccount() {
      var serialNumber = serialNumberFn();
      sendPeRequest(serialNumber, '', 'getUosAccount');

      return new Promise(function (resolve, reject) {
        window.callbackResult = function (returnSerialNumber, result, err) {
          console.log('---getUosAccount---');
          console.log(JSON.stringify(result));
          console.log(err);
          if (returnSerialNumber == serialNumber) {
            if(err) {
               reject(err);
            } else resolve((result));
          }
        };
      });
    }
  }, {
    key: "getWalletWithAccount",
    value: function getWalletWithAccount() {
      var serialNumber = serialNumberFn();

      sendPeRequest(serialNumber, '', 'getWalletWithAccount');

      return new Promise(function (resolve, reject) {
        window.callbackResult = function (returnSerialNumber, result) {
          if (returnSerialNumber == serialNumber) {
            resolve(JSON.parse(result));
          }
        };
      });
    }
  }, {
    key: "getUosBalance",
    value: function getUosBalance(params) {
      var serialNumber = serialNumberFn();
      var Jparams = JSON.stringify(params);

      sendPeRequest(serialNumber, Jparams, 'getUosBalance');


      return new Promise(function (resolve, reject) {
        window.callbackResult = function (returnSerialNumber, result) {
          if (returnSerialNumber == serialNumber) {
            resolve(JSON.parse(result));
          }
        };
      });
    }
  }, {
    key: "getUosAccountInfo",
    value: function getUosAccountInfo(params) {
      var serialNumber = serialNumberFn();
      var Jparams = JSON.stringify(params);

      sendPeRequest(serialNumber, Jparams, 'getUosAccountInfo');

      return new Promise(function (resolve, reject) {
        window.callbackResult = function (returnSerialNumber, result) {
          if (returnSerialNumber == serialNumber) {
            resolve(JSON.parse(result));
          }
        };
      });
    }
  }, {
    key: "getTransactionById",
    value: function getTransactionById(params) {
      var serialNumber = serialNumberFn();
      var Jparams = JSON.stringify(params);

      sendPeRequest(serialNumber, Jparams, 'getTransactionById');

      return new Promise(function (resolve, reject) {
        window.callbackResult = function (returnSerialNumber, result) {
          if (returnSerialNumber == serialNumber) {
            resolve(JSON.parse(result));
          }
        };
      });
    }
  }, {
    key: "pushActions",
    value: function pushActions(params) {
      var serialNumber = params.serialNumber || serialNumberFn();
      var Jparams = JSON.stringify(params);

      sendPeRequest(serialNumber, Jparams, 'pushActions');

      return new Promise(function (resolve, reject) {
        window.callbackResult = function (returnSerialNumber, result, err) {
          console.log('---pushActions---');
          console.log(JSON.stringify(result));
          console.log(err);
          if (returnSerialNumber == serialNumber) {
            if(err) reject(err);
            else resolve((result));
          }
        };
      });
    }
  }, {
    key: "pushTransfer",
    value: function pushTransfer(params) {
      var serialNumber = serialNumberFn();
      var Jparams = JSON.stringify(params);

      sendPeRequest(serialNumber, Jparams, 'pushTransfer');

      return new Promise(function (resolve, reject) {
        window.callbackResult = function (returnSerialNumber, result, err) {
          console.log('---pushTransfer---');
          console.log(JSON.stringify(result));
          console.log(err);
          if (returnSerialNumber == serialNumber) {
              if(err) reject(err);
              else resolve((result));
          }
        };
      });
    }
  }, {
       key: "turnToTxPage",
       value: function turnToTxPage(params) {
         var serialNumber = serialNumberFn();
         var Jparams = JSON.stringify(params);

         sendPeRequest(serialNumber, Jparams, 'turnToTxPage');

         return new Promise(function (resolve, reject) {
           window.callbackResult = function (returnSerialNumber, result, err) {
             if (returnSerialNumber == serialNumber) {
                 if(err) reject(err);
                 else resolve((result));
             }
           };
         });
       }
     }]);

  return pefun;
}();
var pe = new pefun();
var Blockchains = {
  EOS: 'eos',
  ETH: 'eth',
  TRX: 'trx',
  UOS: 'uos'
};

var Network =
function () {
  function Network() {
    var _name = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';

    var _protocol = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 'https';

    var _host = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : '';

    var _port = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : 0;

    var blockchain = arguments.length > 4 && arguments[4] !== undefined ? arguments[4] : Blockchains.UOS;
    var chainId = arguments.length > 5 && arguments[5] !== undefined ? arguments[5] : '';

    _classCallCheck(this, Network);

    this.name = _name;
    this.protocol = _protocol;
    this.host = _host;
    this.port = _port;
    this.blockchain = blockchain;
    this.chainId = chainId.toString();
  }

  _createClass(Network, [{
    key: "isValid",
    value: function isValid() {
      return this.protocol.length && this.host.length && this.port || this.chainId.length;
    }
  }, {
    key: "hostport",
    value: function hostport() {
      return "".concat(this.host).concat(this.port ? ':' : '').concat(this.port);
    }
  }], [{
    key: "placeholder",
    value: function placeholder() {
      return new Network();
    }
  }, {
    key: "fromJson",
    value: function fromJson(json) {
      var p = Object.assign(Network.placeholder(), json);
      p.chainId = p.chainId ? p.chainId.toString() : '';
      return p;
    }
  }]);

  return Network;
}();
var BLOCKCHAIN_SUPPORT = 'blockchain_support';
var Plugin =
function () {
  function Plugin() {
    var _name = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';

    var _type = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : '';

    _classCallCheck(this, Plugin);

    this.name = _name;
    this.type = _type;
  }

  _createClass(Plugin, [{
    key: "isSignatureProvider",
    value: function isSignatureProvider() {
      return this.type === BLOCKCHAIN_SUPPORT;
    }
  }], [{
    key: "placeholder",
    value: function placeholder() {
      return new Plugin();
    }
  }, {
    key: "fromJson",
    value: function fromJson(json) {
      return Object.assign(Plugin.placeholder(), json);
    }
  }]);

  return Plugin;
}();
var PluginRepositorySingleton =
function () {
  function PluginRepositorySingleton() {
    _classCallCheck(this, PluginRepositorySingleton);

    this.plugins = [];
  }

  _createClass(PluginRepositorySingleton, [{
    key: "loadPlugin",
    value: function loadPlugin(plugin) {
      if (!this.plugin(plugin.name)) this.plugins.push(plugin);
    }
  }, {
    key: "signatureProviders",
    value: function signatureProviders() {
      return this.plugins.filter(function (plugin) {
        return plugin.type === BLOCKCHAIN_SUPPORT;
      });
    }
  }, {
    key: "supportedBlockchains",
    value: function supportedBlockchains() {
      return this.signatureProviders().map(function (plugin) {
        return name;
      });
    }
  }, {
    key: "plugin",
    value: function plugin(name) {
      return this.plugins.find(function (plugin) {
        return plugin.name === name;
      });
    }
  }, {
    key: "endorsedNetworks",
    value: function () {
      var _endorsedNetworks = _asyncToGenerator(
      regeneratorRuntime.mark(function _callee2() {
        return regeneratorRuntime.wrap(function _callee2$(_context2) {
          while (1) {
            switch (_context2.prev = _context2.next) {
              case 0:
                _context2.next = 2;
                return Promise.all(this.signatureProviders().map(
                function () {
                  var _ref = _asyncToGenerator(
                  regeneratorRuntime.mark(function _callee(plugin) {
                    return regeneratorRuntime.wrap(function _callee$(_context) {
                      while (1) {
                        switch (_context.prev = _context.next) {
                          case 0:
                            _context.next = 2;
                            return plugin.getEndorsedNetwork();

                          case 2:
                            return _context.abrupt("return", _context.sent);

                          case 3:
                          case "end":
                            return _context.stop();
                        }
                      }
                    }, _callee);
                  }));

                  return function (_x) {
                    return _ref.apply(this, arguments);
                  };
                }()));

              case 2:
                return _context2.abrupt("return", _context2.sent);

              case 3:
              case "end":
                return _context2.stop();
            }
          }
        }, _callee2, this);
      }));

      function endorsedNetworks() {
        return _endorsedNetworks.apply(this, arguments);
      }

      return endorsedNetworks;
    }()
  }]);

  return PluginRepositorySingleton;
}();
var PluginRepository = new PluginRepositorySingleton();
var throwNoAuth = function throwNoAuth() {};
var checkForExtension = function checkForExtension(resolve) {
  var tries = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 0;
  if (tries > 20) return;
  if (window.usmart.isExtension) return resolve(true);
  setTimeout(function () {
    return checkForExtension(resolve, tries + 1);
  }, 100);
};
var IdentityPE = function IdentityPE(account) {
  _classCallCheck(this, IdentityPE);

  this.hash = '1df7bb65ad53a9eb89b4327a56b1200f3abaf085ffec00af222b9eb7622b0734';
  this.publicKey = 'UOS8NJX2UzUFvbAYH7y1KoZpAAP3zjincBaZnDsuvjQQ4VD1KRLeG';
  this.name = 'pocketUOS';
  this.accounts = [{
    name: account,
    authority: 'active',
    blockchain: 'uos'
  }];
  this.kyc = false;
};
var Index =
function () {
  function Index() {
    _classCallCheck(this, Index);

    this.isExtension = true;
    this.identity = null;
  }

  _createClass(Index, [{
    key: "loadPlugin",
    value: function loadPlugin(plugin) {
      var _this = this;

      var noIdFunc = function noIdFunc() {
        if (!_this.identity) throw new Error('No Identity');
      };

      PluginRepository.loadPlugin(plugin);

      if (plugin.isSignatureProvider()) {
        this[plugin.name] = plugin.signatureProvider(noIdFunc);
        this[plugin.name + 'Hook'] = plugin.hookProvider;
      }
    }
  }, {
    key: "isInstalled",
    value: function () {
      var _isInstalled = _asyncToGenerator(
      regeneratorRuntime.mark(function _callee3() {
        return regeneratorRuntime.wrap(function _callee3$(_context3) {
          while (1) {
            switch (_context3.prev = _context3.next) {
              case 0:
                return _context3.abrupt("return", new Promise(function (resolve) {
                  setTimeout(function () {
                    resolve(false);
                  }, 3000);
                  Promise.race([checkForExtension(resolve)]);
                }));

              case 1:
              case "end":
                return _context3.stop();
            }
          }
        }, _callee3);
      }));

      function isInstalled() {
        return _isInstalled.apply(this, arguments);
      }

      return isInstalled;
    }()
  }, {
    key: "connect",
    value: function () {
      var _connect = _asyncToGenerator(
      regeneratorRuntime.mark(function _callee4(pluginName, options) {
        return regeneratorRuntime.wrap(function _callee4$(_context4) {
          while (1) {
            switch (_context4.prev = _context4.next) {
              case 0:
                return _context4.abrupt("return", new Promise(function (resolve) {
                  if (!pluginName || !pluginName.length) throw new Error('You must specify a name for this connection');
                  options = Object.assign({
                    initTimeout: 10000,
                    linkTimeout: 30000
                  }, options);
                  setTimeout(function () {
                    resolve(false);
                  }, options.initTimeout);
                  checkForExtension(resolve);
                }));

              case 1:
              case "end":
                return _context4.stop();
            }
          }
        }, _callee4);
      }));

      function connect(_x2, _x3) {
        return _connect.apply(this, arguments);
      }

      return connect;
    }()
  }, {
    key: "disconnect",
    value: function disconnect() {}
  }, {
    key: "getIdentity",
    value: function getIdentity(requiredFields) {
      var _this2 = this;

      throwNoAuth();
      return new Promise(function (resolve, reject) {
        pe.getUosAccount().then(function (res) {
          var account = res.data;
          var ids = new IdentityPE(account);
          _this2.identity = ids;
          resolve(ids);
        });
      });
    }
  }, {
    key: "getIdentityFromPermissions",
    value: function getIdentityFromPermissions() {
      var _this3 = this;

      throwNoAuth();
      return new Promise(function (resolve, reject) {
        pe.getUosAccount().then(function (res) {
          var account = res.data;
          var ids = new IdentityPE(account);
          _this3.identity = ids;
          resolve(ids);
        });
      });
    }
  }, {
    key: "forgetIdentity",
    value: function forgetIdentity() {
      var _this4 = this;

      throwNoAuth();
      return new Promise(function (resolve, reject) {
        _this4.identity = null;
        resolve(true);
      });
    }
  }, {
    key: "authenticate",
    value: function authenticate(nonce) {
      var _this5 = this;

      throwNoAuth();
      return new Promise(function (resolve, reject) {
        pe.getUosAccount().then(function (res) {
          var account = res.data;
          var ids = new IdentityPE(account);
          _this5.identity = ids;
          resolve(ids);
        });
      });
    }
  }, {
    key: "getArbitrarySignature",
    value: function getArbitrarySignature(publicKey, data) {
      var whatfor = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : '';
      var isHash = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : false;
      var params = {
        publicKey: publicKey,
        data: data,
        whatfor: whatfor,
        isHash: isHash
      };
      return new Promise(function (resolve, reject) {
        var jsonParams = JSON.stringify(params);
        var signature;
        pe.requestMsgSignature(jsonParams).then(function (res) {
          signature = res.data;
          resolve(signature);
        });
      });
    }
  }, {
    key: "getPublicKey",
    value: function getPublicKey(blockchain) {
      throwNoAuth();
      return 0;
    }
  }, {
    key: "linkAccount",
    value: function linkAccount(publicKey, network) {
      throwNoAuth();
      return 0;
    }
  }, {
    key: "hasAccountFor",
    value: function hasAccountFor(network) {
      throwNoAuth();
      return 0;
    }
  }, {
    key: "suggestNetwork",
    value: function suggestNetwork(network) {
      throwNoAuth();
      return 0;
    }
  }, {
    key: "requestTransfer",
    value: function requestTransfer(network, to, amount) {
      var options = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : {};
      var payload = {
        network: network,
        to: to,
        amount: amount,
        options: options
      };
      return 0;
    }
  }, {
    key: "requestSignature",
    value: function requestSignature(payload) {
      throwNoAuth();
      return 0;
    }
  }, {
    key: "createTransaction",
    value: function createTransaction(blockchain, actions, account, network) {
      throwNoAuth();
      return 0;
    }
  }]);

  return Index;
}();
var proxy = function proxy(dummy, handler) {
  return new Proxy(dummy, handler);
};
var cache = {};
var UsmartUOS =
function (_Plugin) {
  _inherits(UsmartUOS, _Plugin);

  function UsmartUOS() {
    _classCallCheck(this, UsmartUOS);

    return _possibleConstructorReturn(this, _getPrototypeOf(UsmartUOS).call(this, Blockchains.UOS, BLOCKCHAIN_SUPPORT));
  }

  _createClass(UsmartUOS, [{
    key: "signatureProvider",
    value: function signatureProvider() {
      var throwIfNoIdentity = arguments.length <= 0 ? undefined : arguments[0];
      return function (network, _uos) {
        var _options = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};

        network = Network.fromJson(network);
        if (!network.isValid()) throw Error.noNetwork();
        var httpEndpoint = "".concat(network.protocol) + '://' + "".concat(network.hostport());
        var chainId = network.hasOwnProperty('chainId') && network.chainId.length ? network.chainId : _options.chainId;
        return proxy(_uos({
          httpEndpoint: httpEndpoint,
          chainId: chainId
        }), {
          get: function get(uosInstance, method) {
            console.log('method', method, uosInstance);
            var returnedFields = null;
            return function () {
              for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
                args[_key] = arguments[_key];
              }

              if (args.find(function (arg) {
                return arg.hasOwnProperty('keyProvider');
              })) throw Error.usedKeyProvider();

              var signProvider =
              function () {
                var _ref2 = _asyncToGenerator(
                regeneratorRuntime.mark(function _callee5(signargs) {
                  var requiredFields, result, lostresult, multiSigKeyProvider;
                  return regeneratorRuntime.wrap(function _callee5$(_context5) {
                    while (1) {
                      switch (_context5.prev = _context5.next) {
                        case 0:
                          throwIfNoIdentity();
                          requiredFields = args.find(function (arg) {
                            return arg.hasOwnProperty('requiredFields');
                          }) || {
                            requiredFields: {}
                          };
                          result = '';
                          _context5.next = 5;
                          return pe.requestSignature(JSON.stringify(signargs)).then(function (res) {
                            result = res.data.signData;
                            return '';
                          });

                        case 5:
                          lostresult = _context5.sent;

                          if (result) {
                            _context5.next = 8;
                            break;
                          }

                          return _context5.abrupt("return", null);

                        case 8:
                          if (!result.hasOwnProperty('signatures')) {
                            _context5.next = 13;
                            break;
                          }

                          returnedFields = result.returnedFields;
                          multiSigKeyProvider = args.find(function (arg) {
                            return arg.hasOwnProperty('signProvider');
                          });

                          if (multiSigKeyProvider) {
                            result.signatures.push(multiSigKeyProvider.signProvider(signargs.buf, signargs.sign));
                          }

                          return _context5.abrupt("return", result.signatures);

                        case 13:
                          return _context5.abrupt("return", result);

                        case 14:
                        case "end":
                          return _context5.stop();
                      }
                    }
                  }, _callee5);
                }));

                return function signProvider(_x4) {
                  return _ref2.apply(this, arguments);
                };
              }();

              return new Promise(function (resolve, reject) {
                var _uos2;

                (_uos2 = _uos(Object.assign(_options, {
                  httpEndpoint: httpEndpoint,
                  signProvider: signProvider,
                  chainId: chainId
                })))[method].apply(_uos2, args).then(function (result) {
                  if (!result.hasOwnProperty('fc')) {
                    result = Object.assign(result, {
                      returnedFields: returnedFields
                    });
                    resolve(result);
                    return;
                  }

                  var contractProxy = proxy(result, {
                    get: function get(instance, method) {
                      if (method === 'then') return instance[method];
                      return function () {
                        for (var _len2 = arguments.length, args = new Array(_len2), _key2 = 0; _key2 < _len2; _key2++) {
                          args[_key2] = arguments[_key2];
                        }

                        return new Promise(
                        function () {
                          var _ref3 = _asyncToGenerator(
                          regeneratorRuntime.mark(function _callee6(res, rej) {
                            return regeneratorRuntime.wrap(function _callee6$(_context6) {
                              while (1) {
                                switch (_context6.prev = _context6.next) {
                                  case 0:
                                    instance[method].apply(instance, args).then(function (actionResult) {
                                      res(Object.assign(actionResult, {
                                        returnedFields: returnedFields
                                      }));
                                    })["catch"](rej);

                                  case 1:
                                  case "end":
                                    return _context6.stop();
                                }
                              }
                            }, _callee6);
                          }));

                          return function (_x5, _x6) {
                            return _ref3.apply(this, arguments);
                          };
                        }());
                      };
                    }
                  });
                  resolve(contractProxy);
                })["catch"](function (error) {
                  return reject(error);
                });
              });
            };
          }
        });
      };
    }
  }]);

  return UsmartUOS;
}(Plugin);
function inject() {
  window.usmart = new Index();
  window.usmart.loadPlugin(new UsmartUOS());
  window.pe = pe;
  document.dispatchEvent(new CustomEvent('ulordLoaded'));
};

inject();