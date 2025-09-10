import React, { useState, useEffect, useCallback, useMemo } from "react";
import {
  Container,
  Title,
  Table,
  NumberInput,
  Select,
  Group,
  LoadingOverlay,
  Alert,
  Paper,
  Pagination,
  Stack,
  Text,
  Button,
  Flex,
} from "@mantine/core";
import { IconSearch } from "@tabler/icons-react";
import { useMovies } from "../hooks";

const MoviesList: React.FC = () => {
  const {
    movies,
    loading,
    error,
    changePage,
    updateFilters,
    loadMovies,
    filters,
  } = useMovies();

  const [yearFilter, setYearFilter] = useState<number | "">("");
  const [winnerFilter, setWinnerFilter] = useState<string>("");

  useEffect(() => {
    loadMovies(filters);
  }, [filters, loadMovies]);

  const handleYearChange = useCallback((value: string | number) => {
    setYearFilter(typeof value === "number" ? value : "");
  }, []);

  const handleWinnerChange = useCallback((value: string | null) => {
    setWinnerFilter(value || "");
  }, []);

  const handleSearch = useCallback(() => {
    updateFilters({
      year: yearFilter || undefined,
      winner: winnerFilter ? winnerFilter === "true" : undefined,
      page: 0,
    });
  }, [yearFilter, winnerFilter, updateFilters]);

  const winnerOptions = [
    { value: "", label: "Todos" },
    { value: "true", label: "Sim" },
    { value: "false", label: "Não" },
  ];

  const isEmpty = useMemo(
    () => movies.content.length === 0 && !loading,
    [movies.content, loading]
  );

  if (error) {
    return (
      <Container size="xl">
        <Alert title="Erro" color="red">
          {error}
        </Alert>
      </Container>
    );
  }

  return (
    <Container size="xl" p="md">
      <Stack gap="md">
        <Title order={2} c="#333">
          Lista de Filmes
        </Title>

        {/* Seção de Filtros */}
        <Paper shadow="xs" p="md" withBorder>
          <Text size="sm" fw={500} mb="md">
            Filtros
          </Text>
          <Flex gap="md" align="end">
            <NumberInput
              label="Ano"
              placeholder="Digite o ano"
              value={yearFilter}
              onChange={handleYearChange}
              min={1900}
              max={2024}
              style={{ width: 150 }}
            />
            <Select
              label="Vencedor"
              data={winnerOptions}
              value={winnerFilter}
              onChange={handleWinnerChange}
              style={{ width: 150 }}
            />
            <Button
              leftSection={<IconSearch size={16} />}
              onClick={handleSearch}
              variant="filled"
            >
              Pesquisar
            </Button>
          </Flex>
        </Paper>

        <Paper shadow="xs" p="md" withBorder>
          <LoadingOverlay visible={loading} />

          <Table striped highlightOnHover>
            <Table.Thead>
              <Table.Tr>
                <Table.Th>
                  <Text size="sm" fw={500}>
                    ID
                  </Text>
                </Table.Th>
                <Table.Th>
                  <Text size="sm" fw={500}>
                    Ano
                  </Text>
                </Table.Th>
                <Table.Th>
                  <Text size="sm" fw={500}>
                    Título
                  </Text>
                </Table.Th>
                <Table.Th>
                  <Text size="sm" fw={500}>
                    Vencedor?
                  </Text>
                </Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {movies.content.map((movie) => (
                <Table.Tr key={movie.id}>
                  <Table.Td>{movie.id}</Table.Td>
                  <Table.Td>{movie.year}</Table.Td>
                  <Table.Td>{movie.title}</Table.Td>
                  <Table.Td>{movie.winner ? "Sim" : "Não"}</Table.Td>
                </Table.Tr>
              ))}
            </Table.Tbody>
          </Table>

          {isEmpty && (
            <Text c="dimmed" ta="center" py="xl">
              Nenhum filme encontrado
            </Text>
          )}

          {movies.totalPages > 1 && (
            <Group justify="space-between" mt="md">
              <Text size="sm" c="dimmed">
                Mostrando {movies.content.length} de {movies.totalElements}{" "}
                filmes
              </Text>
              <Pagination
                total={movies.totalPages}
                value={movies.number + 1}
                onChange={(page) => changePage(page - 1)}
                size="sm"
              />
            </Group>
          )}
        </Paper>
      </Stack>
    </Container>
  );
};

export default MoviesList;
