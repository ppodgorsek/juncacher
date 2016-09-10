# JUncacher, the Java cache invalidation project

JUncacher is a cache invalidation tool for Java programs which allows to have a single point of eviction/update for all caches of an application (Varnish, Spring CacheManager, Solr, others).

Caches can have many forms:
* Spring CacheManager (usually ehcache)
* Varnish
* Solr
* JMS queues (to evict entries from distant systems)
* others can be easily implemented.

[![Codeship Status](https://codeship.com/projects/7fc26c70-a2cf-0133-3d2b-5219d091b483/status?branch=master)](https://codeship.com/projects/129036)
[![Coverage Status](https://coveralls.io/repos/github/ppodgorsek/juncacher/badge.svg?branch=master)](https://coveralls.io/github/ppodgorsek/juncacher?branch=master)
[![Dependency Badge](https://www.versioneye.com/user/projects/56a63e5d1b78fd0039000179/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56a63e5d1b78fd0039000179)

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

For both steps, there is a single entry point: the invalidation manager. It is in charge of the collection and invalidation itself. All calls from other parts of the application should be done to the manager to keep a consistent way of invalidating.

### Identifying and collecting changes

There are many ways to identify changes, such a service to collect changes is called an invalidation collector.

Two possibilities regarding changes:
* Every time an element is modified, the corresponding invalidation entry is created and passed to the collector.
* Elements have a last modification date that allows to dynamically fetch the changes from the data source. This could also be read from a JMS queue for example.

The invalidation manager has two methods to collect entries:
* void addInvalidationEntries(Collection<InvalidationEntry> entries)
* void addInvalidationEntry(InvalidationEntry entry)

![Collecting invalidation entries](https://github.com/ppodgorsek/juncacher/blob/master/src/doc/uml/generated/collect_invalidation_entries_sequence.png)

### Triggering the invalidation

The invalidation manager is the only element which it is necessary to interact with from other parts of the application.
It has a single method to trigger the invalidation: void processEntries()

![Processing invalidation entries](https://github.com/ppodgorsek/juncacher/blob/master/src/doc/uml/generated/process_invalidation_entries_sequence.png)

The processors/collectors having the same colour go together.

The invalidation of an entry is a bit more complex than just a single method call. For example, according to the type of entry:
* Spring CacheManager: the cache regions will probably be different,
* Varnish: the BAN/PURGE/GET URLs will probably be different too. 

This problem is solved in processors by using strategies that will indicate what to invalidate according to the type of entry.

![Invalidation processor strategies](https://github.com/ppodgorsek/juncacher/blob/master/src/doc/uml/generated/invalidation_processor_strategies_activity.png)

### Consuming changes

After an invalidation has been performed, the invalidated element must be consumed in order to avoid processing it again during the next iteration.

It is possible that an invalidation fails therefore we don’t want to remove elements from the queue as soon as they are read in order to allow retries. If an error occurs during the invalidation, the element is not removed and it will be retried during the following iteration.

### Invalidating several layers of cache

A common use case is to have several layers of cache that must be correctly kept up-to-date, for example a Spring Cache (usually ehcache) and an HTTP cache (possibly Varnish).

A processor is linked to one type of cache (Spring, Varnish, etc) and will be in charge of performing the invalidation for this type of cache. Processors can be chained to invalidate a full stack.

Each processor has its own associated collector in order to have finer control over the invalidation in case retries are necessary due to errors in one layer.

Processors can be chained to evict each level of cache, starting by the lower level and going outwards.

![Chain of invalidation processors](https://github.com/ppodgorsek/juncacher/blob/master/src/doc/uml/generated/invalidation_processor_chain_activity.png)

## How to use this project

All artefacts of this project are available on Maven’s central repository, which makes it easy to use in your projects.

If you are using Maven, simply declare the following dependencies:
* juncacher-core:  
`    <dependency>`  
`        <groupId>com.github.ppodgorsek</groupId>`  
`        <artifactId>juncacher-core</artifactId>`  
`        <version>${juncacher.version}</version>`  
`    </dependency>`

* juncacher-springcachemanager:  
`    <dependency>`  
`        <groupId>com.github.ppodgorsek</groupId>`  
`        <artifactId>juncacher-springcachemanager</artifactId>`  
`        <version>${juncacher.version}</version>`  
`    </dependency>`

* juncacher-varnish:  
`    <dependency>`  
`        <groupId>com.github.ppodgorsek</groupId>`  
`        <artifactId>juncacher-varnish</artifactId>`  
`        <version>${juncacher.version}</version>`  
`    </dependency>`

`juncacher-core` is the only mandatory one, the others are optional and depend on what layers of cache your project has.
