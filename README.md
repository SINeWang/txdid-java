

# Derid: A Distributed ID Generator with Time and X-Dimension

## Source

[GitHub][1]

## Features

* decentralization

* Binary Encoding

* 64bits fixed-length

## T1Did64Generator: 64bits Time + 1 Dimension ID Generator

* supports up to 2048 (12bits) instances for a single cluster
* supports up to 524288 (19bits) ids for a single instance per seconds


## T2Did64Generator: 64bits Relation + 2 Dimension ID Generator

* 7 build-in typical generate strategy

| mode | cluster instances | id / instance / seconds |
| ------| ----------- | --------------------------- |
| 0     | 4096  | 2048 |
| 1     | 2048  | 2048 |
| 2     | 1024  | 2048 |
| 3     | 512   | 2048 |
| 4     | 512   | 1024 |
| 5     | 256   | 1024 |
| 6     | 128   | 1024 |
| 7     | 128   | 512  |

[1]:	https://github.com/sinewang/txdid-java
