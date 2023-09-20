import FilteredTable from '../filteredtable/FilteredTable';
import config from './RebuildingKits.config';

class RebuildingKits extends FilteredTable {
  constructor({ el }) {
    super({
      el,
      config
    });
  }
}

export default RebuildingKits;
