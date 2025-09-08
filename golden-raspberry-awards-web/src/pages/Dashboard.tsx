import React, { useEffect } from 'react';
import { Grid, Alert, Container, Title, Stack } from '@mantine/core';
import { useDashboard, useWinnersSearch } from '../hooks';
import YearsWithMultipleWinnersPanel from '../components/Dashboard/YearsWithMultipleWinnersPanel';
import TopStudiosPanel from '../components/Dashboard/TopStudiosPanel';
import ProducerIntervalsPanel from '../components/Dashboard/ProducerIntervalsPanel';
import MovieWinnersByYearPanel from '../components/Dashboard/MovieWinnersByYearPanel';

const Dashboard: React.FC = () => {
  const {
    yearsWithMultipleWinners,
    studiosWithWinCount,
    producerIntervals,
    loading,
    error,
    loadDashboardData,
  } = useDashboard();

  const {
    searchYear,
    winnersByYear,
    loading: winnersLoading,
    error: winnersError,
    handleYearChange,
    handleYearBlur,
  } = useWinnersSearch();

  useEffect(() => {
    loadDashboardData();
  }, [loadDashboardData]);

  const top3Studios = studiosWithWinCount.slice(0, 3);
  const displayError = error || winnersError;

  if (displayError) {
    return (
      <Container size="xl">
        <Alert title="Erro" color="red">
          {displayError}
        </Alert>
      </Container>
    );
  }

  return (
    <Container size="xl" p="md">
      <Stack gap="md">
        <Title order={2} c="#333">
          Dashboard
        </Title>
        <Grid>
          <Grid.Col span={6}>
            <YearsWithMultipleWinnersPanel
              years={yearsWithMultipleWinners}
              isLoading={loading.yearsWithMultipleWinners}
            />
          </Grid.Col>
          <Grid.Col span={6}>
            <TopStudiosPanel studios={top3Studios} isLoading={loading.studiosWithWinCount} />
          </Grid.Col>
          <Grid.Col span={6}>
            <ProducerIntervalsPanel intervals={producerIntervals} isLoading={loading.producerIntervals} />
          </Grid.Col>
          <Grid.Col span={6}>
            <MovieWinnersByYearPanel
              winners={winnersByYear}
              searchYear={searchYear}
              isLoading={winnersLoading}
              onYearChange={handleYearChange}
              onBlur={handleYearBlur}
            />
          </Grid.Col>
        </Grid>
      </Stack>
    </Container>
  );
};

export default Dashboard;