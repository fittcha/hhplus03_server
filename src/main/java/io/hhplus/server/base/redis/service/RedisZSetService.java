package io.hhplus.server.base.redis.service;

import io.hhplus.server.base.config.SpringConfig;
import io.hhplus.server.base.redis.RedisConstants;
import io.hhplus.server.base.redis.dto.RedisZSetReqDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisZSetService {

    private final StringRedisTemplate stringRedisTemplate;

    private final SpringConfig springConfig;
    private ZSetOperations<String, String> zSetOperations;

    @PostConstruct
    private void init() {
        zSetOperations = stringRedisTemplate.opsForZSet();
    }

    public Boolean zSetAdd(RedisZSetReqDto.ZAdd reqDto) {
        return zSetOperations.add(this.getRedisNameSpace() + reqDto.getKey(), reqDto.getMember(), reqDto.getScore());
    }

    // Long은 삭제된 요소의 개수
    public Long zSetRem(RedisZSetReqDto.ZRem reqDto) {
        return zSetOperations.remove(this.getRedisNameSpace() + reqDto.getKey(), reqDto.getMember());
    }

    public Long zSetRank(RedisZSetReqDto.ZRank reqDto) {
        return zSetOperations.rank(this.getRedisNameSpace() + reqDto.getKey(), reqDto.getMember());
    }

    public Long zSetRemoveRangeByScore(RedisZSetReqDto.ZRemoveRangeByScore reqDto) {
        Range<Double> range = Range.closed(reqDto.getMin(), reqDto.getMax());
        return zSetOperations.removeRangeByScore(this.getRedisNameSpace() + reqDto.getKey(), range.getLowerBound().getValue().get(), range.getUpperBound().getValue().get());
    }

    public Long zSetSize(RedisZSetReqDto.ZCard reqDto) {
        return zSetOperations.size(this.getRedisNameSpace() + reqDto.getKey());
    }

    public void zSetAddAll(RedisZSetReqDto.ZAddTuples reqDto) {
        zSetOperations.add(this.getRedisNameSpace() + reqDto.getKey(), reqDto.getTuples());
    }

    public Set<String> zSetRangeByStartEnd(RedisZSetReqDto.ZRangeByStartEnd reqDto){
        return zSetOperations.range(this.getRedisNameSpace() + reqDto.getKey(), reqDto.getStart(), reqDto.getEnd());
    }

    public Set<String> zSetRangeByScore(RedisZSetReqDto.ZRangeByScore reqDto) {
        Range<Double> range = Range.closed(reqDto.getMin(), reqDto.getMax());
        return zSetOperations.rangeByScore(this.getRedisNameSpace() + reqDto.getKey(), range.getLowerBound().getValue().get(), range.getUpperBound().getValue().get());
    }

    public Set<String> getKeysWithPattern(String pattern){
        return stringRedisTemplate.keys(this.getRedisNameSpace() + pattern + ":*");
    }

    public String getRedisNameSpace() {
        return springConfig.getActiveProfile() + ":" + RedisConstants.REDIS_NAME_SPACE_WAITING + ":";
    }
}
