import { storageUtil } from '../common/common';
import {EQ_TYPE,EQ_FILTERS,EQ_LOCAL_STORAGE,TPCOOKIEUSERTEM} from './constants';

export default {
  emptyFilterChipInLocalStorage(type){
    if(type===EQ_TYPE){
      window.localStorage.removeItem(EQ_LOCAL_STORAGE);
    }
  },
  setFilterChipInLocalStorage(type,filterObject,countryCode){
    const filterChipsStorageObj={};
    if(type===EQ_TYPE){
      filterChipsStorageObj[EQ_FILTERS.COUNTRY] = countryCode;
      filterChipsStorageObj[EQ_FILTERS.EQUIPMENTNAME] = filterObject[EQ_FILTERS.EQUIPMENTNAME];
      filterChipsStorageObj[EQ_FILTERS.SERIALNUMBER] = filterObject[EQ_FILTERS.SERIALNUMBER];
      if(filterObject[EQ_FILTERS.EQUIPMENTSTATUSDESC] !== undefined && filterObject[EQ_FILTERS.EQUIPMENTSTATUSDESC].length > 0 ) {
        filterChipsStorageObj[EQ_FILTERS.EQUIPMENTSTATUSDESC] = filterObject[EQ_FILTERS.EQUIPMENTSTATUSDESC];
      }
      if(filterObject[EQ_FILTERS.CUSTOMER] !== undefined && filterObject[EQ_FILTERS.CUSTOMER].length > 0 ) {
        filterChipsStorageObj[EQ_FILTERS.CUSTOMER] = filterObject[EQ_FILTERS.CUSTOMER];
      }
      //filterChipsStorageObj[EQ_FILTERS.EQUIPMENTSTATUSDESC] = filterObject[EQ_FILTERS.EQUIPMENTSTATUSDESC];
      if(filterObject[EQ_FILTERS.EQUIPMENTTYPE] !== undefined && filterObject[EQ_FILTERS.EQUIPMENTTYPE].length > 0 ) {
        filterChipsStorageObj[EQ_FILTERS.EQUIPMENTTYPE] = filterObject[EQ_FILTERS.EQUIPMENTTYPE];
      }
      if(filterObject[EQ_FILTERS.LINECODE] !== undefined && filterObject[EQ_FILTERS.LINECODE].length > 0 ) {
        filterChipsStorageObj[EQ_FILTERS.LINECODE] = filterObject[EQ_FILTERS.LINECODE];
      }
      if(storageUtil.getCookie(TPCOOKIEUSERTEM)){
        filterChipsStorageObj[EQ_FILTERS.USERNAME]= storageUtil.getCookie(TPCOOKIEUSERTEM);
      }
      else{
        filterChipsStorageObj[EQ_FILTERS.USERNAME]= '';
      }
      window.localStorage.setItem(EQ_LOCAL_STORAGE,JSON.stringify(filterChipsStorageObj));
    }
  },
  getFiltersValueFromLocalStorage(type){
    const filterChipsStorageObj={};
    if(type===EQ_TYPE){
      const cacheEquipmentListFiltersObj=JSON.parse(window.localStorage.getItem(EQ_LOCAL_STORAGE));
      if(cacheEquipmentListFiltersObj!==null){
        if (Object.keys(cacheEquipmentListFiltersObj).length){
          const currentUser=storageUtil.getCookie(TPCOOKIEUSERTEM)|| '';
          if(currentUser===cacheEquipmentListFiltersObj[EQ_FILTERS.USERNAME]){
            filterChipsStorageObj[EQ_FILTERS.COUNTRY] = cacheEquipmentListFiltersObj[EQ_FILTERS.COUNTRY]||'';
            filterChipsStorageObj[EQ_FILTERS.EQUIPMENTNAME] = cacheEquipmentListFiltersObj[EQ_FILTERS.EQUIPMENTNAME]||'';
            filterChipsStorageObj[EQ_FILTERS.SERIALNUMBER] = cacheEquipmentListFiltersObj[EQ_FILTERS.SERIALNUMBER]||'';
            if(cacheEquipmentListFiltersObj[EQ_FILTERS.EQUIPMENTSTATUSDESC] !== undefined && cacheEquipmentListFiltersObj[EQ_FILTERS.EQUIPMENTSTATUSDESC].length > 0 ) {
              filterChipsStorageObj[EQ_FILTERS.EQUIPMENTSTATUSDESC] = cacheEquipmentListFiltersObj[EQ_FILTERS.EQUIPMENTSTATUSDESC];
            }
            if(cacheEquipmentListFiltersObj[EQ_FILTERS.CUSTOMER] !== undefined && cacheEquipmentListFiltersObj[EQ_FILTERS.CUSTOMER].length > 0 ) {
            //filterChipsStorageObj[EQ_FILTERS.EQUIPMENTSTATUSDESC] = cacheEquipmentListFiltersObj[EQ_FILTERS.EQUIPMENTSTATUSDESC] || '';
              filterChipsStorageObj[EQ_FILTERS.CUSTOMER] = cacheEquipmentListFiltersObj[EQ_FILTERS.CUSTOMER];
            }
            if(cacheEquipmentListFiltersObj[EQ_FILTERS.EQUIPMENTTYPE] !== undefined && cacheEquipmentListFiltersObj[EQ_FILTERS.EQUIPMENTTYPE].length > 0 ) {
              filterChipsStorageObj[EQ_FILTERS.EQUIPMENTTYPE] = cacheEquipmentListFiltersObj[EQ_FILTERS.EQUIPMENTTYPE];
            }
            if(cacheEquipmentListFiltersObj[EQ_FILTERS.LINECODE] !== undefined && cacheEquipmentListFiltersObj[EQ_FILTERS.LINECODE].length > 0 ) {
              filterChipsStorageObj[EQ_FILTERS.LINECODE] = cacheEquipmentListFiltersObj[EQ_FILTERS.LINECODE];
            }
          }
          else{
            window.localStorage.removeItem(EQ_LOCAL_STORAGE);
          }
        }
      }
    }
    return filterChipsStorageObj;
  }
};