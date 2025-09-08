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
} from "@mantine/core";
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

  const handleYearBlur = useCallback(() => {
    updateFilters({ year: yearFilter || undefined, page: 0 });
  }, [yearFilter, updateFilters]);

  const handleWinnerChange = useCallback(
    (value: string | null) => {
      const newWinnerFilter = value || "";
      setWinnerFilter(newWinnerFilter);
      updateFilters({
        winner: newWinnerFilter ? newWinnerFilter === "true" : undefined,
        page: 0,
      });
    },
    [updateFilters]
  );

  const winnerOptions = [
    { value: "", label: "Sim/Não" },
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
          Lista de filmes
        </Title>

        <Paper shadow="xs" p="md" withBorder>
          <LoadingOverlay visible={loading} />

          <Table striped highlightOnHover>
            <Table.Thead>
              <Table.Tr>
                <Table.Th>
                  <Stack gap="xs">
                    <Text size="sm" fw={500}>
                      ID
                    </Text>
                  </Stack>
                </Table.Th>
                <Table.Th>
                  <Stack gap="xs">
                    <Text size="sm" fw={500}>
                      Ano
                    </Text>
                    <NumberInput
                      placeholder="Filtrar por ano"
                      value={yearFilter}
                      onChange={handleYearChange}
                      onBlur={handleYearBlur}
                      min={1900}
                      max={2024}
                      size="xs"
                      style={{ width: 120 }}
                    />
                  </Stack>
                </Table.Th>
                <Table.Th>
                  <Stack gap="xs">
                    <Text size="sm" fw={500}>
                      Título
                    </Text>
                  </Stack>
                </Table.Th>
                <Table.Th>
                  <Stack gap="xs">
                    <Text size="sm" fw={500}>
                      Vencedor?
                    </Text>
                    <Select
                      data={winnerOptions}
                      value={winnerFilter}
                      onChange={handleWinnerChange}
                      size="xs"
                      style={{ width: 100 }}
                    />
                  </Stack>
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
