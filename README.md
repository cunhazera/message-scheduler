# message-scheduler project

This project schedules a new message!

Don't forget to use the correct data and don't try to schedule a new message for the past :)

# Running the app

## The database

It's mandatory to use the application!

`docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name message_db -e POSTGRES_USER=message_user -e POSTGRES_PASSWORD=message_pass -e POSTGRES_DB=message_db -p 5432:5432 postgres:10.5`

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## The app

If you want to run it with a jar, just compile the project with `mvn clean install`.
It produces the `message-scheduler-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.

The application is now runnable using `java -jar target/message-scheduler-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

I strongly recommend you to run it as a native application! Much faster and lightweight!

The only problem is that it's harder do compile in native mode, it requires a lot from your machine.

For that reason, there is a directory called `.native` which contains the native file to run the project. The file is `message-scheduler-1.0-SNAPSHOT-runner`. You just need to run it as an executable file.

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.

# How to use the application

There are 3 different endpoints available for you:

## Scheduling a new message

If you send a `POST /messages` with a JSON as payload you'll schedule a new message to be sent in the future.
Payload example:
```
{
    "sendDate": "2031-05-03T07:05:03",
    "recipient": "teste12345",
    "content": "teste",
    "channel": "SMS"
}
```

## Accessing a scheduled message

Sending a `GET /messages/{id}` request you'll get your scheduled message as response. It contains all the data you first used to schedule.

## Deleting a scheduled message
In case you want to delete a scheduled message you just need to send a `DELETE /messages{id}` request. The scheduled message will be removed from the database. Take care! It can't be undone.