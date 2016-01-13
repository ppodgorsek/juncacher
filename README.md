# Welcome to the Cache Invalidation project!

This project is a cache invalidation tool for Java programs which allows to have a single point of eviction/update for all caches of an application (Varnish, Spring Cache Manager, Solr, others).

The supported cache types are currently:
* Spring Cache Manager
* Varnish
* others can be easily implemented.

Any questions/comments/new ideas? Feel free to contribute!

## How does it work?

In order to represent what has to be invalidated, invalidation entries have been created containing for example:
* a type,
* an ID,
* a reference to the element having changed,
* any other useful information.

Cache invalidation happens in two steps:
* identifying what the changes are,
* performing the invalidation itself.

### Identifying and collecting changes

There are many ways to identify changes, such a service to collect changes is called an invalidation logger.

Two possibilities regarding changes:
* Every time an element is modified, the corresponding invalidation entry is created and passed to the logger.
* Elements have a last modification date that allows to dynamically fetch the changes from the data source. This could also be read from a JMS queue for example.

The invalidation entries will be stored by/read from the logger.

![Collecting invalidation entries](https://github.com/ppodgorsek/cache-invalidation/blob/master/src/doc/uml/generated/collect_invalidation_entries.png)

### Triggering the invalidation

The invalidation processor is in charge of the invalidation. All calls from other parts of the application should be done to the processor to keep a consistent way of invalidating.

![Processing invalidation entries](https://github.com/ppodgorsek/cache-invalidation/blob/master/src/doc/uml/generated/process_invalidation_entries.png)

The helpers/loggers of the same colour go together.

### Consuming changes

After an invalidation has been performed, the invalidated element must be consumed in order to avoid processing it again during the next iteration.

It is possible that an invalidation fails therefore we don’t want to remove elements from the queue as soon as they are read in order to allow retries. If an error occurs during the invalidation, the element is not removed and it will be retried during the following iteration.

### Invalidating several layers of cache

A common use case is to have several layers of cache that must be correctly kept up-to-date, for example a Spring Cache (usually ehcache) and an HTTP cache (possibly Varnish).

A helper is linked to one type of cache (Spring, Varnish, etc) and will be in charge of performing the invalidation for this type of cache. Helpers can be chained to invalidate a full stack.

Each helper has its own associated logger in order to have finer control over the invalidation in case retries are necessary due to errors in one layer.

## How to use this project

All artefacts of this project are available on Maven’s central repository, which makes it easy to use in your project.

If you are using Maven, simply declare the following dependencies:
* cache-invalidation-core:  
`    <dependency>`  
`        <groupId>com.github.ppodgorsek.cache</groupId>`  
`        <artifactId>cache-invalidation-core</artifactId>`  
`        <version>${cache-invalidation.version}</version>`  
`    </dependency>`

* cache-invalidation-spring:  
`    <dependency>`  
`        <groupId>com.github.ppodgorsek.cache</groupId>`  
`        <artifactId>cache-invalidation-spring</artifactId>`  
`        <version>${cache-invalidation.version}</version>`  
`    </dependency>`

* cache-invalidation-varnish:  
`    <dependency>`  
`        <groupId>com.github.ppodgorsek.cache</groupId>`  
`        <artifactId>cache-invalidation-varnish</artifactId>`  
`        <version>${cache-invalidation.version}</version>`  
`    </dependency>`

`cache-invalidation-core` is the only mandatory one, the others are optional and depend on what layers of cache your project has.
