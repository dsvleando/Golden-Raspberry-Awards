package br.com.outsera.gra;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.outsera.gra.application.port.inbound.ProducerUseCase;
import br.com.outsera.gra.shared.vo.ProducerAwardIntervals;

@SpringBootTest
@ActiveProfiles("test")
class ProducerAwardIntervalsValidationIT {

    @Autowired
    private ProducerUseCase producerUseCase;

    @Test
    void shouldValidateProducerAwardIntervalsAccordingToProposal() {
        ProducerAwardIntervals intervals = producerUseCase.getProducerAwardIntervals();

        assertThat(intervals.getMin()).isNotEmpty();
        assertThat(intervals.getMax()).isNotEmpty();

        if (!intervals.getMin().isEmpty() && !intervals.getMax().isEmpty()) {
            int minInterval = intervals.getMin().get(0).getInterval();
            int maxInterval = intervals.getMax().get(0).getInterval();
            assertThat(minInterval).isLessThan(maxInterval);
        }

        if (!intervals.getMin().isEmpty()) {
            int minInterval = intervals.getMin().get(0).getInterval();
            assertThat(intervals.getMin())
                    .allMatch(interval -> interval.getInterval() == minInterval);
        }

        if (!intervals.getMax().isEmpty()) {
            int maxInterval = intervals.getMax().get(0).getInterval();
            assertThat(intervals.getMax())
                    .allMatch(interval -> interval.getInterval() == maxInterval);
        }
    }

    @Test
    void shouldValidateSpecificProducerData() {
        ProducerAwardIntervals intervals = producerUseCase.getProducerAwardIntervals();

        assertThat(intervals.getMin()).isNotEmpty();
        
        assertThat(intervals.getMax()).isNotEmpty();

        intervals.getMin().forEach(minInterval -> {
            boolean foundInMax = intervals.getMax().stream()
                    .anyMatch(maxInterval -> minInterval.getProducer().equals(maxInterval.getProducer()));
            assertThat(foundInMax).isFalse();
        });

        intervals.getMax().forEach(maxInterval -> {
            boolean foundInMin = intervals.getMin().stream()
                    .anyMatch(minInterval -> maxInterval.getProducer().equals(minInterval.getProducer()));
            assertThat(foundInMin).isFalse();
        });
    }

    @Test
    void shouldValidateIntervalCalculations() {
        ProducerAwardIntervals intervals = producerUseCase.getProducerAwardIntervals();

        intervals.getMin().forEach(interval -> {
            int calculatedInterval = interval.getFollowingWin() - interval.getPreviousWin();
            assertThat(interval.getInterval()).isEqualTo(calculatedInterval);
        });

        intervals.getMax().forEach(interval -> {
            int calculatedInterval = interval.getFollowingWin() - interval.getPreviousWin();
            assertThat(interval.getInterval()).isEqualTo(calculatedInterval);
        });

        intervals.getMin().forEach(interval -> {
            assertThat(interval.getPreviousWin()).isLessThan(interval.getFollowingWin());
        });

        intervals.getMax().forEach(interval -> {
            assertThat(interval.getPreviousWin()).isLessThan(interval.getFollowingWin());
        });
    }
}
