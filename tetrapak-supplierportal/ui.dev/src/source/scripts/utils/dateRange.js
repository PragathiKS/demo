export const getFilterDateRange = (month) => {
  const currentDate = new Date();
  const monthsAgo = new Date(currentDate.getFullYear(), currentDate.getMonth() - Number(month), currentDate.getDate());

  // Format the date as a string (YYYY-MM-DDTHH:MM:SS)
  const formattedDate = `&fromdatetime=${monthsAgo.toISOString().slice(0, 11)}00:00:00&todatetime=${currentDate.toISOString().slice(0, 19)}`;

  return formattedDate;
} ;
