package ssodamproject.server.staticFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssodamproject.server.common.api.ApiResponseDto;
import ssodamproject.server.common.api.ResponseUtils;
import ssodamproject.server.common.enumSet.ErrorType;
import ssodamproject.server.common.exception.RestApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaticService {

    private final StaticRepository staticRepository;

    @Transactional
    public ApiResponseDto<StaticDto> getVisit()
    {
        StaticDto staticDto = new StaticDto();
        StaticEntity staticEntity = staticRepository.findById(1L)
                        .orElseThrow(()-> new RestApiException(ErrorType.NOT_FOUND));

        staticDto.setTotalVisit(staticEntity.getTotalVisit());
        staticDto.setTodayVisit(staticEntity.getTodayVisit());

        staticEntity.setTodayVisit(staticEntity.getTodayVisit()+1);
        staticEntity.setTotalVisit(staticEntity.getTotalVisit()+1);

        staticRepository.save(staticEntity);
        return ResponseUtils.ok(staticDto, null);



    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateCompetitionDday() {
        StaticEntity staticEntity = staticRepository.findById(0L).orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND));
        staticEntity.setTodayVisit(0);
        staticRepository.save(staticEntity);
    }
}
