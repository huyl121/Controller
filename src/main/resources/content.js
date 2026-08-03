// =================================================================
// OKX 仓位监控 Stalker (多线程防睡眠 + 纯净不刷新 + CPU极致优化版)
// =================================================================

const sleep = ms => new Promise(res => setTimeout(res, ms));

// 1. 触发 25% 缩放
chrome.runtime.sendMessage({ action: "ZOOM_PAGE" });
console.log(`%c[页面缩放] 25% 缩放指令已发送...`, "color: #00ffff; font-weight: bold;");

// 跨时空常驻内存字典
if (!window.STALKER_RAW_MAP) {
  window.STALKER_RAW_MAP = new Map();
}

console.log(`%c[插件已注入] 正在等待 10 秒以确保初始页面数据加载完毕...`, "color: #ff00ff; font-weight: bold;");

setTimeout(() => {
  console.log(`%c[不倒翁启动] 10秒完毕！已废除网页刷新，CPU 极限轻量化算法已就位。`, "color: #00ff00; font-weight: bold;");

  // 每 7 天安全重载一次
  setInterval(() => {
    location.reload();
  }, 7 * 24 * 60 * 60 * 1000);

  // ----------------------------------------------------------------
  // Web Worker 独立线程死守 5 秒脉搏（不占用主线程，绝不睡眠）
  // ----------------------------------------------------------------
  const blob = new Blob([`
    setInterval(() => {
      self.postMessage('TICK_5S');
    }, 5000);
  `], { type: 'text/javascript' });

  const worker = new Worker(URL.createObjectURL(blob));

  worker.onmessage = async (e) => {
    if (e.data === 'TICK_5S') {
      await executePositionScan();
    }
  };

  // ----------------------------------------------------------------
  // 【优化点 1】: 将抖动频率由 400ms 大幅放慢至 1500ms
  // 不再让浏览器高频重绘，改为每 1.5 秒轻轻向下、再向上滚动一次，同样能激活虚拟列表，
  // 但能让浏览器的布局引擎（Layout）得到充分休息，大幅降低 CPU 占用！
  // ----------------------------------------------------------------
  let scrollDirection = 1;
  setInterval(() => {
    const scrollContainer = document.querySelector('.okui-table-scroll-behavior') ||
        document.querySelector('[class*="table-scroll"]') ||
        window;

    if (scrollContainer === window) {
      window.scrollBy(0, scrollDirection * 6);
    } else {
      scrollContainer.scrollBy(0, scrollDirection * 6);
    }

    scrollDirection = -scrollDirection;
  }, 1500); // 👈 从 400ms 放慢到 1500ms

  // ----------------------------------------------------------------
  // 【优化点 2】: 放弃万恶的 `*` 暴力通配符，改用精准 DOM 节点收窄过滤
  // ----------------------------------------------------------------
  async function executePositionScan() {
    // 连续采样完整快照，避免页面渲染瞬间读到“有效但错误”的数字。
    const readPositionSnapshot = () => {
      let hasParseError = false;

      const targetElements = Array.from(document.querySelectorAll('div, tr'));
      const rawCards = targetElements.filter(el => {
        if (!el.innerText || el.innerText.length > 1000) return false;
        const hasHeaderFeature = el.innerText.includes('永续') || el.innerText.includes('交割');
        const hasBodyFeature = el.innerText.includes('持仓量');
        return hasHeaderFeature && hasBodyFeature;
      });

      const cards = rawCards.filter(card => {
        return !Array.from(card.querySelectorAll('div, tr')).some(child => {
          if (child === card || !child.innerText) return false;
          const childHasHeader = child.innerText.includes('永续') || child.innerText.includes('交割');
          const childHasBody = child.innerText.includes('持仓量');
          return childHasHeader && childHasBody;
        });
      });

      const snapshotMap = new Map();

      for (const card of cards) {
        const text = card.innerText;
        let coin = '未知币种';
        let direction = '未知';
        let amount = '未知';

        const coinMatch = text.match(/([A-Z0-9\- \/]+)\s*(永续|交割)/i);
        if (coinMatch) coin = coinMatch[1].trim();

        if (text.includes('多') || text.includes('买')) direction = 'long';
        else if (text.includes('空') || text.includes('卖')) direction = 'short';

        const amountMatch = text.match(/持仓量[^\d]*([\d\.]+)/);
        if (amountMatch) amount = amountMatch[1].trim();

        if (coin !== '未知币种' && direction !== '未知' && amount !== '未知' && amount !== '') {
          const uniqueKey = `${coin}_${direction}`;
          snapshotMap.set(uniqueKey, {
            'symbol': coin,
            'positionSide': direction,
            'positionAmount': amount
          });
        } else {
          hasParseError = true;
        }
      }

      const list = Array.from(snapshotMap.values()).sort((a, b) => {
        const aKey = `${a.symbol}_${a.positionSide}`;
        const bKey = `${b.symbol}_${b.positionSide}`;
        return aKey.localeCompare(bKey);
      });

      return { list, hasParseError };
    };

    const snapshotsEqual = (a, b) => JSON.stringify(a) === JSON.stringify(b);

    const snapshots = [];
    let hasParseError = false;
    const maxSamples = 3;

    for (let i = 0; i < maxSamples; i++) {
      const snapshot = readPositionSnapshot();
      snapshots.push(snapshot.list);
      hasParseError = hasParseError || snapshot.hasParseError;

      if (i < maxSamples - 1) {
        await sleep(500);
      }
    }

    const isStable = snapshots.every(snapshot => snapshotsEqual(snapshot, snapshots[0]));
    const positionList = isStable ? snapshots[0] : [];

    if (isStable && !hasParseError) {
      window.STALKER_RAW_MAP.clear();
      for (const item of positionList) {
        const uniqueKey = `${item.symbol}_${item.positionSide}`;
        window.STALKER_RAW_MAP.set(uniqueKey, item);
      }
    }

    if (hasParseError || !isStable) {
      console.log(`%c[拦截发送] 连续 3 次采样未稳定或存在未渲染完整的数据，等待下一个 5 秒周期。`, "color: #ff5555;");
      return;
    }

    // 4. 打印结构化控制台大表并准备发送数据
    if (positionList.length > 0) {
      console.log(`%c[脉搏响应] ${new Date().toLocaleTimeString()} | 实时去重全量持仓 [ ${positionList.length} ] 个。`, "color: #00ff00; font-weight: bold;");
      console.table(positionList);
      sendToBackend(positionList);
    } else {
      console.log(`%c[空仓监控] ${new Date().toLocaleTimeString()} | 确定为空仓，向后端同步空仓。`, "color: #ffcc00;");
      sendToBackend([]);
    }

    function sendToBackend(data) {
      fetch('http://localhost:8181/genDan', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      })
          .then(response => {
            if (response.ok) {
              console.log(`%c[接口调用] 成功推送到后端!`, "color: #00aaff;");
            }
          })
          .catch(error => console.error(`[接口失败] 无法连接到本地 8181 端口。`));
    }
  }

}, 10 * 1000);
