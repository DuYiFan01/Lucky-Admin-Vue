// 从iconfont.json文件中提取图标名称
const iconJson = require('@/assets/app/icons/iconfont.json')

// 提取所有图标的font_class作为图标名称
const icons = iconJson.glyphs.map(item => {
  return iconJson.css_prefix_text + item.font_class
})

export default icons
