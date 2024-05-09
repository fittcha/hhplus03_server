package io.hhplus.server.base.redis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class RedisZSetReqDto {

    private RedisZSetReqDto() {
    }

    @Getter
    @Setter
    @ToString
    public static class ZAdd {
        private String key;
        private double score;
        private String member;

        public ZAdd(String key, double score, String member) {
            this.key = key;
            this.score = score;
            this.member = member;
        }

        public static ZAdd of(String key, double score, String member) {
            return new ZAdd(key, score, member);
        }
    }

    @Getter
    @Setter
    @ToString
    public static class ZAddTuples {
        private String key;
        private Set<ZSetOperations.TypedTuple<String>> tuples;

        private ZAddTuples(String key, double score, Set<String> member) {
            this.key = key;
            this.tuples = member.stream()
                    .map(mem -> new DefaultTypedTuple<>(mem, score))
                    .collect(Collectors.toSet());
        }

        public static ZAddTuples of(String key, double score, Set<String> member) {
            return new ZAddTuples(key, score, member);
        }
    }

    @Getter
    @Setter
    @ToString
    public static class ZRem {
        private String key;
        private String member;

        private ZRem(String key, String member) {
            this.key = key;
            this.member = member;
        }

        public static ZRem of(String key, String member) {
            return new ZRem(key, member);
        }
    }

    @Getter
    @Setter
    @ToString
    public static class ZRank {
        private String key;
        private String member;

        public ZRank(String key, String member) {
            this.key = key;
            this.member = member;
        }

        public static ZRank of(String key, String member) {
            return new ZRank(key, member);
        }
    }

    @Getter
    @Setter
    @ToString
    public static class ZRemoveRangeByScore {
        private String key;
        private double min;
        private double max;

        public ZRemoveRangeByScore(String key, double min, double max) {
            this.key = key;
            this.min = min;
            this.max = max;
        }

        public static ZRemoveRangeByScore of(String key, double min, double max) {
            return new ZRemoveRangeByScore(key, min, max);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ZRemoveRangeByScore that = (ZRemoveRangeByScore) o;
            return Double.compare(getMin(), that.getMin()) == 0 && Double.compare(getMax(), that.getMax()) == 0 && Objects.equals(getKey(), that.getKey());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getKey(), getMin(), getMax());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class ZRangeByScore {
        private String key;
        private double min;
        private double max;

        private ZRangeByScore(String key, double min, double max) {
            this.key = key;
            this.min = min;
            this.max = max;
        }

        public static ZRangeByScore of(String fullKey, double min, double max) {
            return new ZRangeByScore(fullKey, min, max);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ZRangeByScore that = (ZRangeByScore) o;
            return Objects.equals(getKey(), that.getKey());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getKey());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class ZCard {

        private String key;

        public ZCard(String key) {
            this.key = key;
        }

        public static ZCard of(String key) {
            return new ZCard(key);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ZCard zCard = (ZCard) o;
            return Objects.equals(getKey(), zCard.getKey());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getKey());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class ZRangeByStartEnd {
        private String key;
        private long start;
        private long end;

        private ZRangeByStartEnd(String key, long start, long end) {
            this.key = key;
            this.start = start;
            this.end = end;
        }

        public static ZRangeByStartEnd of(String fullKey, long start, long end) {
            return new ZRangeByStartEnd(fullKey, start, end);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ZRangeByStartEnd that = (ZRangeByStartEnd) o;
            return getStart() == that.getStart() && getEnd() == that.getEnd() && Objects.equals(getKey(), that.getKey());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getKey(), getStart(), getEnd());
        }
    }

}
