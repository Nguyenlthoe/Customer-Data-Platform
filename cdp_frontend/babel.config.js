module.exports = {
    presets: ['@babel/preset-react'],
    plugins: [
      [
        'import',
        {
          libraryName: 'antd',
          style: 'css',
        },
      ],
    ],
  };
  