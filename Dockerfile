FROM gradle:8.5

WORKDIR /app

COPY /app .

RUN gradle installDist

CMD ./build/install/app/bin/app
