// 监听来自 content.js 的消息
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.action === "ZOOM_PAGE" && sender.tab) {
    // 0.25 代表 25% 的原生缩放比例
    chrome.tabs.setZoom(sender.tab.id, 0.25, () => {
      console.log(`成功将标签页 ${sender.tab.id} 缩放至 25%`);
    });
  }
});