import React from "react";
import {
  Paper,
  Title,
  Table,
  LoadingOverlay,
  Stack,
  Text,
  TextInput,
} from "@mantine/core";
import { IconSearch } from "@tabler/icons-react";
import { WinnersByYearResponse } from "../../api";

interface MovieWinnersByYearPanelProps {
  winners: WinnersByYearResponse[];
  searchYear: string;
  isLoading: boolean;
  onYearChange: (value: string) => void;
  onSearch: () => void;
}

const MovieWinnersByYearPanel: React.FC<MovieWinnersByYearPanelProps> = ({
  winners,
  searchYear,
  isLoading,
  onYearChange,
  onSearch,
}) => (
  <Paper shadow="xs" p="md" withBorder>
    <Title order={4} mb="md" c="#333">
      Listar Vencedores de Filmes por Ano
    </Title>
    <Stack gap="md">
      <TextInput
        label="Ano"
        placeholder="Digite o ano"
        value={searchYear}
        onChange={(event) => onYearChange(event.target.value.replace(/[^0-9]/g, ""))}
        min={1900}
        max={2024}
        rightSection={<IconSearch size={16} onClick={onSearch} />}
      />
      <LoadingOverlay visible={isLoading} />
      <Table striped highlightOnHover>
        <Table.Thead>
          <Table.Tr>
            <Table.Th>ID</Table.Th>
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
          Nenhum vencedor encontrado para o ano {searchYear}
        </Text>
      )}
    </Stack>
  </Paper>
);

export default MovieWinnersByYearPanel;
