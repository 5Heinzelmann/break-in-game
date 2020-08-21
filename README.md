# break-in-game

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
Make sure to start Judge0 code execution API before.
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).

## Start judge0 API locally

```
# Download sources
wget https://github.com/judge0/api/releases/download/v1.10.0/judge0-v1.10.0.zip
unzip judge0-v1.10.0.zip

# Start databases
docker-compose up -d db redis

# Start service
docker-compose up

# Tutorial: https://github.com/judge0/api/blob/master/CHANGELOG.md#with-http
```
