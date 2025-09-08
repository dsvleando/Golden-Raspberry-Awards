import React from 'react';
import { Paper, Title, Table, LoadingOverlay, Stack, NumberInput, Text } from '@mantine/core';
import { WinnersByYearResponse } from '../../api';

interface MovieWinnersByYearPanelProps {
  winners: WinnersByYearResponse[];
  searchYear: number | "";
  isLoading: boolean;
  onYearChange: (value: string | number) => void;
  onBlur: () => void;
}

const MovieWinnersByYearPanel: React.FC<MovieWinnersByYearPanelProps> = ({
  winners,
  searchYear,
  isLoading,
  onYearChange,
  onBlur,
}) => (
  <Paper shadow="xs" p="md" withBorder>
    <Title order={4} mb="md" c="#333">
      Listar vencedores de filmes por ano
    </Title>
    <Stack gap="md">
      <NumberInput
        placeholder="Pesquisar por ano"
        value={searchYear}
        onChange={onYearChange}
        onBlur={onBlur}
        min={1900}
        max={2024}
      />
      <LoadingOverlay visible={isLoading} />
      <Table striped highlightOnHover>
        <Table.Thead>
          <Table.Tr>
            <Table.Th>Id</Table.Th>
            <Table.Th>Ano</Table.Th>
            <Table.Th>Título</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {winners.map((movie) => (
            <Table.Tr key={movie.id}>
              <Table.Td>{movie.id}</Table.Td>
              <Table.Td>{movie.year}</Table.Td>
              <Table.Td>{movie.title}</Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>
      {searchYear && winners.length === 0 && !isLoading && (
        <Text c="dimmed" ta="center" py="xl">
          Nenhum vencedor encontrado para {searchYear}
        </Text>
      )}
    </Stack>
  </Paper>
);

export default MovieWinnersByYearPanel;
