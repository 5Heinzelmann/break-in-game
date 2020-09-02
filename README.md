# Break-in-game Local Start

## I. Project setup
```
npm install
```

## II. Start judge0 code execution API locally

```
cd judge0

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

## Download new judge0
Neue Versionen nach der 1.10.0?: https://github.com/judge0/api/releases
- Für Windows kann der Link direkt im Browser (ohne den Befehl wget) eingegeben werden, das unzip kann händisch erfolgen.
