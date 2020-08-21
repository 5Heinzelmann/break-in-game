# Break-in-game Local Start

## I. Project setup
```
npm install
```

## II. Start judge0 code execution API locally

```
# Download sources
wget https://github.com/judge0/api/releases/download/v1.10.0/judge0-v1.10.0.zip
unzip judge0-v1.10.0.zip
cd judge0-v1.10.0

# Start databases
docker-compose up -d db redis

# Start service
docker-compose up
```
Tutorial: https://github.com/judge0/api/blob/master/CHANGELOG.md#with-http


## III. Compiles and hot-reloads for development
```
npm run serve
```